package br.com.vvedana.imarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by User on 02/07/2017.
 */

public class ProdutoDetalhe extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView txtProduto;
    private TextView txtValor;
    private EditText txtQtde;
    private Button btnAddCarrinho;
    private Button btnAddLista;
    private ImageView imgProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);
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
        final Produto prod = (Produto) intent.getSerializableExtra("PROD");
        final String loja = (String) intent.getSerializableExtra("LOJA");

        txtProduto = (TextView) findViewById(R.id.txtProdDetalhe);
        txtValor = (TextView) findViewById(R.id.txtValorDetalhe);
        txtQtde = (EditText) findViewById(R.id.txtQtde);
        imgProd = (ImageView) findViewById(R.id.imgProdDetalhe);
        btnAddCarrinho = (Button) findViewById(R.id.botaoAddCarrinho);
        btnAddLista = (Button) findViewById(R.id.botaAddLista);

        txtProduto.setText(prod.getNome());
        txtValor.setText("R$ " + prod.getPrecoMin());
        new DownloadImageTask(imgProd).execute(prod.getUrl());

        btnAddCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter bd = new DBAdapter(ProdutoDetalhe.this);
                bd.open();

                bd.insereProdCarrinho(prod.getNome(),prod.getPrecoMin(), prod.getUrl(), Integer.parseInt(txtQtde.getText().toString()),loja);
                bd.close();
                Toast.makeText(ProdutoDetalhe.this, "Incluído no Carrinho!", Toast.LENGTH_LONG).show();
            }
        });

        btnAddLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter bd = new DBAdapter(ProdutoDetalhe.this);
                bd.open();
                bd.insereProdLista(prod.getNome(),prod.getPrecoMin(), prod.getUrl());
                bd.close();
            }
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
