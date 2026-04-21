package model;

import java.io.Serializable;

public class Partida implements Serializable {

    private int id;
    private Usuario usuario;
    private String modo;
    private int tempo;
    private String resultado;
    private String dataPartida;

    public Partida() {
    }

    public Partida(Usuario usuario, String modo, int tempo, String resultado, String dataPartida) {
        this.usuario = usuario;
        this.modo = modo;
        this.tempo = tempo;
        this.resultado = resultado;
        this.dataPartida = dataPartida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(String dataPartida) {
        this.dataPartida = dataPartida;
    }

    @Override
    public String toString() {
        return "Partida " + this.id + " - " + this.modo;
    }
}
