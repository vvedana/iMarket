package br.com.vvedana.imarket;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinicius on 26/06/2017.
 */

public class JsonUtil {

    public static List<Produto> getProdutos(String json) throws JSONException {

        List<Produto> lista = new ArrayList<>();

        JSONObject jobj = new JSONObject(json); // objeto json que vem da requisicao

        JSONArray jsonArray = jobj.getJSONArray("products"); // pega o array q esta no json (jobj) chamado products

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i); // aqui pego objeto a objeto do array, no caso, cada produto
            long id = object.getLong("id");
            String nomeCompleto = object.getString("name");
            String nome =  object.getString("name");
            double precoMin = object.getDouble("priceMin");
            double precoMax =  object.getDouble("priceMax");
            JSONObject thumbnail = object.getJSONObject("thumbnail"); // como dentro do array produto tem mais um objeto, pego ele
            String url = thumbnail.getString("url"); // e pego a url que esta no objeto (thumbnail) dentro do outro objetio (produto i)
            Log.i("PROD",nomeCompleto + " / " + url);
            lista.add(new Produto(id, nomeCompleto, nome, precoMin, precoMax, url));
        }

        return lista;
    }

    public static List<Loja> getLojas(String json) throws JSONException{
        List<Loja> lista = new ArrayList<>();

        JSONObject jobj = new JSONObject(json); // objeto json que vem da requisicao

        JSONArray jsonArray = jobj.getJSONArray("stores"); // pega o array q esta no json (jobj) chamado stores

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            int id = object.getInt("id");
            String nome = object.getString("name");
            String url = object.getString("thumbnail");
            lista.add(new Loja(id, nome, url));
        }

        return lista;

    }

    public static Entrega getEntrega(String json) throws JSONException{
        Entrega entrega = new Entrega();

        JSONObject jobj = new JSONObject(json); // objeto json que vem da requisicao

        JSONArray dest = jobj.getJSONArray("destination_addresses");
        entrega.setDestino(dest.getString(0));

        JSONArray ori = jobj.getJSONArray("origin_addresses");
        entrega.setOrigem(ori.getString(0));

        JSONArray rows = jobj.getJSONArray("rows");
        JSONObject object = rows.getJSONObject(0);
        JSONArray elements = object.getJSONArray("elements");
        JSONObject object1 = elements.getJSONObject(0);
        JSONObject distancia = object1.getJSONObject("distance");
        JSONObject tempo = object1.getJSONObject("duration");
        entrega.setDistancia(distancia.getInt("value"));
        entrega.setTempo(tempo.getInt("value"));

        return entrega;
    }
}
