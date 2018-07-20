package br.com.vvedana.imarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 01/07/2017.
 */

public class DBAdapter {

    private static final String TABELA_USUARIO = "usuario";
    private static final String KEY_USUARIO_ROWID = "_id";
    private static final String KEY_NOME    = "nome";
    private static final String KEY_EMAIL   = "email";
    private static final String KEY_SENHA   = "senha";
    private static final String KEY_ENDERECO = "endereco";

    private static final String TABELA_CARRINHO = "carrinho";
    public static final String KEY_CARRINHO_ROWID = "_id";
    public static final String KEY_PRODUTO = "produto";
    public static final String KEY_VALOR = "valor";
    public static final String KEY_URL = "url";
    public static final String KEY_QTDE = "quantidade";
    public static final String KEY_LOJA = "loja";

    public static final String TABELA_LISTA = "lista";
    public static final String KEY_LISTA_ROWID = "_id";
    public static final String KEY_LISTA_PRODUTO = "listaProd";
    public static final String KEY_LISTA_VALOR = "listaValor";
    public static final String KEY_LISTA_URL = "listaUrl";



    private static final String DATABASE_NAME = "iMarketDB";

    private static final int DATABASE_VERSION = 3;

    private static final String CRIA_TAB_CARRINHO =
            "create table "+TABELA_CARRINHO+" " +
                    "("+KEY_CARRINHO_ROWID+" integer primary key autoincrement, " +
                    " "+KEY_PRODUTO+" text not null," +
                    " "+KEY_VALOR+" real not null," +
                    " "+KEY_QTDE+" real not null," +
                    " "+KEY_LOJA+" text not null," +
                    " "+KEY_URL+" text not null);";

    private static final String CRIA_TAB_USUARIO = "create table "+TABELA_USUARIO+" " +
            "("+KEY_USUARIO_ROWID+" integer primary key autoincrement, " +
            " "+KEY_NOME+" text not null," +
            " "+KEY_EMAIL+" text not null," +
            " "+KEY_SENHA+" text not null," +
            " "+KEY_ENDERECO+" text not null);";

    private static final String CRIA_TAB_LISTA = "create table "+TABELA_LISTA+" " +
            "("+KEY_LISTA_ROWID+" integer primary key autoincrement, " +
            " "+KEY_LISTA_PRODUTO+" text not null," +
            " "+KEY_LISTA_URL+" text not null," +
            " "+KEY_LISTA_VALOR+" text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            try{
                db.execSQL(CRIA_TAB_CARRINHO);
                db.execSQL(CRIA_TAB_USUARIO);
                db.execSQL(CRIA_TAB_LISTA);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_CARRINHO);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_LISTA);
            onCreate(db);
        }
    }

    // *******************************************************************************
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public long insereUsuario(String nome, String email, String senha, String endereco){
        ContentValues dados = new ContentValues();
        dados.put(KEY_NOME, nome);
        dados.put(KEY_EMAIL, email);
        dados.put(KEY_SENHA, senha);
        dados.put(KEY_ENDERECO, endereco);
        return db.insert(TABELA_USUARIO, null, dados);
    }

    public long insereProdCarrinho(String produto, double valor, String url, int qtde, String loja){
        ContentValues dados = new ContentValues();
        dados.put(KEY_PRODUTO, produto);
        dados.put(KEY_VALOR, valor);
        dados.put(KEY_URL, url);
        dados.put(KEY_QTDE, qtde);
        dados.put(KEY_LOJA, loja);
        return db.insert(TABELA_CARRINHO, null, dados);
    }

    public long insereProdLista(String produto, double valor, String url){
        ContentValues dados = new ContentValues();
        dados.put(KEY_LISTA_PRODUTO, produto);
        dados.put(KEY_LISTA_VALOR, valor);
        dados.put(KEY_LISTA_URL, url);
        return db.insert(TABELA_LISTA, null, dados);
    }

    public Cursor getCarrinho(){
        String colunas[] ={KEY_CARRINHO_ROWID,KEY_PRODUTO,KEY_VALOR,KEY_URL, KEY_QTDE, KEY_LOJA};
        return db.query(TABELA_CARRINHO,colunas, null, null, null, null, null);
    }

    public void ezvaziaCarrinho(){
        db.delete(TABELA_CARRINHO,"1=1",null);
    }

    public void deletaItemCarrinho(long rowid){
        db.delete(TABELA_CARRINHO,KEY_CARRINHO_ROWID + "=" + rowid,null);
    }

    public void atualizaQtde(int qtde, long rowid){
        ContentValues dados = new ContentValues();
        dados.put(KEY_QTDE,qtde);
        db.update(TABELA_CARRINHO,dados,KEY_CARRINHO_ROWID + "=" + rowid, null);
    }

    public Cursor getUsuario(String usuario, String senha) {
        String colunas[] = {KEY_USUARIO_ROWID, KEY_NOME};
        return db.query(TABELA_USUARIO,colunas,KEY_EMAIL + " = " + usuario + " and " + KEY_SENHA + " = "+ senha,null, null, null, null);
    }

}
