package br.com.vvedana.imarket;

/**
 * Created by User on 03/07/2017.
 */

public class Entrega {

    private String origem;
    private String destino;
    private int tempo;
    private int distancia;

    public Entrega(String origem, String destino, int tempo, int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.tempo = tempo;
        this.distancia = distancia;
    }

    public Entrega(){

    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
}
