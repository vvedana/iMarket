package br.com.vvedana.imarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 02/07/2017.
 */

public class CarrinhoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CarrinhoAdapter carrinhoAdapter;
    ListView carrinhoListView;
    TextView txtTotItens;
    TextView txtTotCarrinho;
    Button btnFinaliza;
    int totItens = 0;
    double totCarrinho = 0;
    String loja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carrinhoListView = (ListView) findViewById(R.id.listViewCarrinho);

        carrinhoAdapter = new CarrinhoAdapter(CarrinhoActivity.this, new ArrayList<Carrinho>());
        carrinhoListView.setAdapter(carrinhoAdapter);

        txtTotItens = (TextView) findViewById(R.id.txtTotItens);
        txtTotCarrinho = (TextView) findViewById(R.id.txtValorTot);
        btnFinaliza = (Button) findViewById(R.id.btnFinaliza);

        btnFinaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totItens <=0 ){
                    Toast.makeText(CarrinhoActivity.this, "Não há itens no carrinho!", Toast.LENGTH_LONG).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarrinhoActivity.this);

                    builder.setMessage("Confirma finalização da compra?")
                            .setTitle("Atenção!");

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(CarrinhoActivity.this, FinalizaActivity.class);
                            intent.putExtra("LOJA", loja);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        carrinhoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Carrinho carrinho = (Carrinho) parent.getItemAtPosition(position);
                final EditText input = new EditText(CarrinhoActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                //input.setText(carrinho.getQtde());
                AlertDialog.Builder builder = new AlertDialog.Builder(CarrinhoActivity.this);


                builder.setMessage("O que deseja fazer?")
                        .setTitle("Opções:");

                builder.setView(input);

                builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBAdapter bd = new DBAdapter(CarrinhoActivity.this);
                        bd.open();
                        bd.deletaItemCarrinho(carrinho.getIdProd());
                        bd.close();
                        new ObtemCarrinho().execute();
                    }
                });
                builder.setNegativeButton("Alterar Qtde", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBAdapter bd = new DBAdapter(CarrinhoActivity.this);
                        bd.open();
                        bd.atualizaQtde(Integer.parseInt(input.getText().toString()),carrinho.getIdProd());
                        bd.close();
                        new ObtemCarrinho().execute();

                    }
                });
                builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        new ObtemCarrinho().execute();
    }

    private class ObtemCarrinho extends AsyncTask<Object, Object, Cursor> {
        DBAdapter conexaoDB = new DBAdapter(CarrinhoActivity.this);
        @Override
        protected Cursor doInBackground(Object... params){
            conexaoDB.open();
            return conexaoDB.getCarrinho(); //retorna todas as crianças
        }

        @Override
        protected void onPostExecute(Cursor result){
            ArrayList<Carrinho> arrayList = new ArrayList<>();
            totItens = 0;
            totCarrinho = 0;

            result.moveToFirst();
            for(int i = 0;i<result.getCount();i++){
                Carrinho carrinho = new Carrinho();
                carrinho.setIdProd(result.getLong(result.getColumnIndex(DBAdapter.KEY_CARRINHO_ROWID)));
                carrinho.setNome(result.getString(result.getColumnIndex(DBAdapter.KEY_PRODUTO)));
                carrinho.setValor(result.getDouble(result.getColumnIndex(DBAdapter.KEY_VALOR)));
                carrinho.setUrl(result.getString(result.getColumnIndex(DBAdapter.KEY_URL)));
                carrinho.setQtde(result.getInt(result.getColumnIndex(DBAdapter.KEY_QTDE)));
                loja = result.getString(result.getColumnIndex(DBAdapter.KEY_LOJA));
                totCarrinho = totCarrinho + carrinho.getValor() * carrinho.getQtde();
                arrayList.add(carrinho);
                result.moveToNext();
            }
            totItens = arrayList.size();
            txtTotItens.setText(String.valueOf(totItens));
            txtTotCarrinho.setText("R$ " + String.valueOf(totCarrinho));

            result.close();
            carrinhoAdapter.clear();
            carrinhoAdapter.addAll(arrayList);
            carrinhoAdapter.notifyDataSetChanged();
            conexaoDB.close();
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
            Intent intent = new Intent(CarrinhoActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_carrinho) {
            //Intent intent = new Intent(CarrinhoActivity.this, CarrinhoActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Intent intent = new Intent(CarrinhoActivity.this, InicioActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
