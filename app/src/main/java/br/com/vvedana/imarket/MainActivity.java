package br.com.vvedana.imarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String sourceId = "358059";
    private String token = "1498486089397c1586e0c";
    private GoogleApiClient mGoogleApiClient;
    private ListView listaLoja;
    private LojaAdapter lojaAdapter;

    private TextView txtLoja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listaLoja = (ListView) findViewById(R.id.listViewLoja);
        txtLoja = (TextView) findViewById(R.id.txtLoja);

        lojaAdapter = new LojaAdapter(MainActivity.this, new ArrayList<Loja>());
        listaLoja.setAdapter(lojaAdapter);



        listaLoja.setOnItemClickListener(new listClickListener());



        new LojaTask().execute();


    }

    private class listClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);
            Loja loja = (Loja) parent.getItemAtPosition(position);
            intent.putExtra("LOJA", loja);
            startActivity(intent);
        }
    }


    private class LojaTask extends AsyncTask<String, Void, List<Loja>> {

        @Override
        protected List<Loja> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL("https://sandbox-api.lomadee.com/v2/"+ token + "/store/_all?sourceId=" + sourceId);
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
                return JsonUtil.getLojas(buffer.toString());
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
        protected void onPostExecute(List<Loja> lojas) {
            lojaAdapter.clear();
            lojaAdapter.addAll(lojas);
            lojaAdapter.notifyDataSetChanged();
        }
    }

    private synchronized void callConnection(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");

        Location l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if(l != null){
            Log.i("LOG_LOC", "latitude: "+l.getLatitude());
            Log.i("LOG_LOC", "longitude: "+l.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed("+connectionResult+")");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_loja) {
            // Handle the camera action
            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_carrinho) {
            Intent intent = new Intent(MainActivity.this, CarrinhoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Intent intent = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
