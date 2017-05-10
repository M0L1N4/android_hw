package com.example.alvaromolina.appalvaro;

//..
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

    private EditText txtNombre;
    private Button btnAceptar;

    private Button btnEnviar;
    private EditText txtBDNom;
    private EditText txtBDNum;

    private Button btnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos una referencia a los controles de la interfaz
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        btnAceptar = (Button)findViewById(R.id.btnAceptar);

        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        txtBDNom = (EditText)findViewById(R.id.txtBDnom);
        txtBDNum = (EditText)findViewById(R.id.txtBDnum);

        btnImage = (Button)findViewById(R.id.btnImage);

        //Implementamos el evento click del botón
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, Saluda.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("NOM", txtNombre.getText().toString());

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
            }


        });

        //Implementamos el evento click del botón
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, connexio.class);

                //Creamos la información a pasar entre actividades
                Bundle n = new Bundle();
                n.putString("ID", txtBDNum.getText().toString());

                Bundle b = new Bundle();
                b.putString("NOM", txtBDNom.getText().toString());

                //Añadimos la información al intent
                intent.putExtras(n);
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
            }


        });


        //Implementem el botó Image del menú principal
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, uploadImage.class);

                //Iniciamos la nueva actividad
                System.out.println("\n*-*-*-*-*-*-***Before the intent");
                startActivity(intent);
            }


        });
    }
}
