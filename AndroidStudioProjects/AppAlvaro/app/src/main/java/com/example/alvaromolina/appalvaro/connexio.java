package com.example.alvaromolina.appalvaro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by alvaro.molina on 11/11/16.
 */

public class connexio extends MainActivity {

    private TextView txtEnviatOK;
    private Bundle bundle;
    //    private Connection conn;
    private String id, nom;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if(wifiOn()) {
//            showAlertDialog(MainActivity.this, "Connexió WiFi Ok!",
//                    "Tu Dispositivo tiene Conexion a Wifi.", true);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_connexio);

            //Localizar los controles
            txtEnviatOK = (TextView) findViewById(R.id.txtEnviatOK);

            //Recuperamos la información pasada en el intent
            bundle = this.getIntent().getExtras();

            id = bundle.getString("ID");
            nom = bundle.getString("NOM");

            sqlThread.start();

            if (sqlThread.isAlive()) {
                //Construimos el mensaje a mostrar
                txtEnviatOK.setText("S'ha escrit a la BBDD el ID: " + id
                        + ".\n I també s'ha escrit el NOM: " + nom);
            } else {
                //Construimos el mensaje de ERROR a mostrar
                txtEnviatOK.setText("La connexió és nula, ERROR!");
            }
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            sqlThread.stop(); // tell the thread to stop
            try {
                sqlThread.join(); // wait for the thread to stop
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//        }
//        else{

//        }
    }

    private boolean wifiOn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
    * Per crear una connexió a una Base de dades, a més de retocar el Manifest, hi ha que crear un nou
     * fil d'execució que s'encarregue de la tasca, ja que per defecte no està permesa aquesta operació
     * al fil principal de l'aplicació.
     *
     * Per optimitzar ela present aplicació, hi hauriem de crear un fil connexió a BBDD que estigués
     * viu fins que l'usuari acabara d'utilitzar l'aplicació, i que quan l'usuari tancara l'aplicació,
     * doncs el fil acabara la seva execució i també tancara la connexió a la BBDD.
     *
     * A banda, cada accés a la BBDD (consulta, actualització...) crec que deuria també tenir el seu
     * fil propi, que fes una feina determinada, però per a tal pròpòsit hi hauriem de sincronitzar
     * l'ús de variables des del fil principal (i pare de tots) als fils fills; ahí toca remirar bé
     * la documentació... Per ara, podem utilitzar un accés tipus el que jo he fet aquí XD
    * */


    Thread sqlThread = new Thread() {
        public void run() {

            //Inicialitzem la connexió
            Connection conn = null;
            try {
                //Carreguem el driver
                Class.forName("org.postgresql.Driver");
                System.out.println("\nBefore connection");

                conn = DriverManager.getConnection(
                        "jdbc:postgresql://192.168.0.21:5432/tutto",
                        "postgres", "tesla");
                Statement st = conn.createStatement();

                //Executem la nostra consulta
                st.executeUpdate("INSERT INTO simple values(" + id + ", '" + nom + "');");
//                System.out.println("\nBefore the insert");
//                st.executeUpdate("INSERT INTO fitxa(nom, descrip, foto) values('Alvaro', 'Des del mòbil', lo_import('Logo.png'));");


            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console\n" + e.toString().trim());
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your PostgreSQL JDBC Driver?" +
                        "\nInclude in your library path!");
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("connexio Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

/*************************
 Thread insertThread = new Thread() {
 public void run() {
 //        System.out.println("*-*-*-*-*-*-*-*-*-*\n" + conn + "\n*-*-*-*-*-*-*-*-*-");
 try {
 //Creem el full sobre el que anem a escriure les nostres consultes
 Statement st = conn.createStatement();

 //Executem la nostra consulta
 st.executeUpdate("INSERT INTO simple values(" + id + ", '" + nom + "');");

 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 };

 Thread closeThread = new Thread() {
 public void run() {
 //Tanquem la connexió
 try {
 conn.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 };

 ************************/
}
