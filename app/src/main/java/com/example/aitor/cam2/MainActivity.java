package com.example.aitor.cam2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ip = (EditText) findViewById(R.id.ip);
        final EditText port = (EditText) findViewById(R.id.port);

        Button b = (Button) findViewById(R.id.boton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView t = (TextView) findViewById(R.id.texto);
                int puerto = Integer.valueOf(port.getText().toString());

                HiloCAM hc = new HiloCAM(view.getContext(), new Comunicaciones(ip.getText().toString(),puerto));

                Thread th = new Thread(hc);
                th.start();
                System.out.println("Enviando....");
                t.setText("Enviando...");
            }
        });

        Button b2 = (Button) findViewById(R.id.parar);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView t = (TextView) findViewById(R.id.texto);
                t.setText(VariablesGlobales.cam.toString());

            }
        });

        Button recibir = (Button) findViewById(R.id.recibir);

        recibir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hilo_recepcion hr=new Hilo_recepcion(Integer.valueOf(port.getText().toString()));
                Thread th = new Thread(hr);
                th.start();
            }
        });

    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
