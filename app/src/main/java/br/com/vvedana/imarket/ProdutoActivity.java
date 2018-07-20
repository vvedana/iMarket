package br.com.vvedana.imarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

/**
 * Created by User on 02/07/2017.
 */

public class ProdutoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String sourceId = "358059";
    private String token = "1498486089397c1586e0c";
    private GoogleApiClient mGoogleApiClient;
    private ListView listaProdutos;
    private ProdutoAdapter produtosAdapter;

    private TextView txtProduto;
    private TextView txtValor;
    private TextView txtNomeLoja;
    private EditText txtBuscar;
    private Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
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
        final Loja loja = (Loja) intent.getSerializableExtra("LOJA");

        listaProdutos = (ListView) findViewById(R.id.listView);

        produtosAdapter = new ProdutoAdapter(ProdutoActivity.this, new ArrayList<Produto>());
        listaProdutos.setAdapter(produtosAdapter);

        txtProduto = (TextView) findViewById(R.id.txtProduto);
        txtValor = (TextView) findViewById(R.id.txtValor);
        txtNomeLoja = (TextView) findViewById(R.id.txtNomeLoja);
        txtBuscar = (EditText) findViewById(R.id.txtBusca);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        txtNomeLoja.setText(loja.getNome());

        btnBuscar.setOnClickListener(buscarProdClicked);

        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProdutoActivity.this, ProdutoDetalhe.class);
                Produto prod = (Produto) parent.getItemAtPosition(position);
                intent.putExtra("PROD", prod);
                intent.putExtra("LOJA",loja.getNome());
                startActivity(intent);
            }
        });


    }

    View.OnClickListener buscarProdClicked = new View.OnClickListener(){
        public void onClick(View v){
            new ProdutoTask().execute(txtBuscar.getText().toString());
        }
    };

    private class ProdutoTask extends AsyncTask<String, Void, List<Produto>> {

        @Override
        protected List<Produto> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String busca = params[0];
            busca = busca.replace(" ","");

            try {

                URL url = new URL("https://sandbox-api.lomadee.com/v2/"+ token + "/product/_search?sourceId=" + sourceId + "&keyword=" + busca);
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
                return JsonUtil.getProdutos(buffer.toString());
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
        protected void onPostExecute(List<Produto> itemVideos) {
            produtosAdapter.clear();
            produtosAdapter.addAll(itemVideos);
            produtosAdapter.notifyDataSetChanged();
        }
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
            Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_carrinho) {
            Intent intent = new Intent(ProdutoActivity.this, CarrinhoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Intent intent = new Intent(ProdutoActivity.this, InicioActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
