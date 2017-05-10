package com.example.alvaromolina.appalvaro;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.postgresql.core.Utils;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class uploadImage extends Activity {

    private TextView imgUpOK;
    private Bundle bundle;
    private Image foto;
    private ImageView imgShowOk;

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
        setContentView(R.layout.activity_image);

        //Localizar los controles
        imgUpOK = (TextView) findViewById(R.id.txtImageOK);
        imgShowOk = (ImageView) findViewById(R.id.imageView);

        System.out.println("\n*-*-*-*-*-*-***Before the START");

//        sqlThread.start();
        sqlFilConsulta.start();

        if (sqlFilConsulta.isAlive()) {
            //Construimos el mensaje a mostrar
            imgUpOK.setText("S'ha pujat correctament la imatge");

        } else {
            //Construimos el mensaje de ERROR a mostrar
            imgUpOK.setText("La connexió és nula, ERROR!");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//        sqlThread.stop(); // tell the thread to stop
        try {
            sqlFilConsulta.join(); // wait for the thread to stop
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        }
//        else{

//        }
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


    private Thread sqlFilConsulta = new Thread() {
        public void run() {

            //Inicialitzem la connexió
            Connection conn = null;
            ResultSet rs    = null;
            try {
                //Carreguem el driver
                Class.forName("org.postgresql.Driver");

                conn = DriverManager.getConnection(
                        "jdbc:postgresql://192.168.0.21:5432/tutto",
                        "postgres", "tesla");
                Statement st = conn.createStatement();

                //Executem la nostra consulta
                rs = st.executeQuery("SELECT * FROM tarja;");

                File outputFile      = null;
                FileOutputStream fos = null;
                Bitmap myBitmap      = null;
                byte[] dibuix        = null;


                while (rs.next()) {
                    outputFile = new File("output_" + rs.getString("nom"));
                    //fos = new FileOutputStream(new File(getFilesDir(), String.valueOf(outputFile)));
                     fos = new FileOutputStream(new File(getExternalFilesDir(null),
                             String.valueOf(outputFile)));

                    if (rs.getBytes("foto") != null) {
                        System.out.println("\nInside THE IF "+rs.getBytes("foto").toString());
                        fos.write(rs.getBytes("foto"));
                        dibuix = rs.getBytes("foto");
                    }
                    System.out.println("\n-+-+-+-+-+-+-+-+Inici de la trampa");

                    myBitmap=BitmapFactory.decodeByteArray(dibuix, 0, dibuix.length);
                    imgShowOk = (ImageView) findViewById(R.id.imageView);
                    imgShowOk.setImageBitmap(myBitmap);

                    System.out.println("\n*-*-*-*-*-*-Fi de la trampa");
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    };


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

                    System.out.println("\nBefore the insert");
                    st.executeUpdate("INSERT INTO fitxa(nom, descrip, foto) values('Alvaro', " +
                            "'Des del mòbil', lo_import('/sdcard/Download/Logo.png'));");

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
    }
