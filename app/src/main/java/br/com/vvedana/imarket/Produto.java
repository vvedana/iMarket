package br.com.vvedana.imarket;

import java.io.Serializable;

/**
 * Created by UCS on 28/06/2017.
 */

public class Produto implements Serializable {

    long idProd;
    String nomeCompleto;
    String nome;
    double precoMin;
    double precoMax;
    String url;

    public Produto(long idProd, String nomeCompleto, String nome, double precoMin, double precoMax, String url) {
        this.idProd = idProd;
        this.nomeCompleto = nomeCompleto;
        this.nome = nome;
        this.precoMin = precoMin;
        this.precoMax = precoMax;
        this.url = url;
    }

    public Produto(){

    }

    public long getIdProd() {
        return idProd;
    }

    public void setIdProd(long idProd) {
        this.idProd = idProd;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoMin() {
        return precoMin;
    }

    public void setPrecoMin(double precoMin) {
        this.precoMin = precoMin;
    }

    public double getPrecoMax() {
        return precoMax;
    }

    public void setPrecoMax(double precoMax) {
        this.precoMax = precoMax;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
