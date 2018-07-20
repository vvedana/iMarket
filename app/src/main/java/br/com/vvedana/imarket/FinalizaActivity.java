package br.com.vvedana.imarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by User on 03/07/2017.
 */

public class FinalizaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Entrega entrega;
    TextView txtOrigem;
    TextView txtDestino;
    TextView txtDistancia;
    TextView txtTempo;
    String loja;
    String lojaAux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent = getIntent();
        loja = (String) intent.getSerializableExtra("LOJA");
        lojaAux = loja;
        loja = loja.replace(" ","");

        txtOrigem = (TextView) findViewById(R.id.txtOrigem);
        txtDestino = (TextView) findViewById(R.id.txtDestino);
        txtDistancia = (TextView) findViewById(R.id.txtDistancia);
        txtTempo = (TextView) findViewById(R.id.txtTempo);



        new EntregaTask().execute();



    }

    private class EntregaTask extends AsyncTask<String, Void, Entrega> {

        @Override
        protected Entrega doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Log.i("LOJA",loja);
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+loja+",Caxias+do+Sul,RS&destinations=UCS,Caxias+do+Sul,RS&key=AIzaSyCfyX3MI0BUDgQYaxWDlVuBq1Fd9eX1b8c");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }
                Log.i("AVISOO","FIMM");
                return JsonUtil.getEntrega(buffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Entrega result) {
            entrega = result;
            txtOrigem.setText(lojaAux + ", " + entrega.getOrigem());
            txtDestino.setText(entrega.getDestino());
            float distancia = entrega.getDistancia() / 1000;
            txtDistancia.setText(String.valueOf(distancia)+" km");
            float tempo = (entrega.getTempo() / 60) + 1;
            txtTempo.setText(String.valueOf(tempo));

            DBAdapter bd = new DBAdapter(FinalizaActivity.this);
            bd.open();
            bd.ezvaziaCarrinho();
            bd.close();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
