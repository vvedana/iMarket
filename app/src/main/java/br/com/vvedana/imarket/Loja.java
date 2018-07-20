package br.com.vvedana.imarket;

import java.io.Serializable;

/**
 * Created by User on 02/07/2017.
 */

public class Loja implements Serializable {

    private int id;
    private String nome;
    private String url;

    public Loja(int id, String nome, String url) {
        this.id = id;
        this.nome = nome;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
