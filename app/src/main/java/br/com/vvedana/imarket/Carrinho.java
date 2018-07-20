package br.com.vvedana.imarket;

/**
 * Created by User on 02/07/2017.
 */

public class Carrinho {

    private long idProd;
    private String nome;
    private double valor;
    private String url;
    private int qtde;

    public Carrinho(long idProd, String nome, double valor, String url, int qtde) {
        this.idProd = idProd;
        this.nome = nome;
        this.valor = valor;
        this.url = url;
        this.qtde = qtde;
    }

    public Carrinho(){

    }

    public long getIdProd() {
        return idProd;
    }

    public void setIdProd(long idProd) {
        this.idProd = idProd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }
}
