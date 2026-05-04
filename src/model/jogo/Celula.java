package model.jogo;

public class Celula {

    private boolean temBomba;
    private boolean aberta;
    private EstadoMarcacao estadoMarcacao;
    private int minasAoRedor;

    public enum EstadoMarcacao {
        VAZIO,
        BANDEIRA,
        INTERROGACAO
    }

    public Celula() {
        this.temBomba = false;
        this.aberta = false;
        this.estadoMarcacao = EstadoMarcacao.VAZIO;
        this.minasAoRedor = 0;
    }

    public boolean isTemBomba() {
        return this.temBomba;
    }

    public void setTemBomba(boolean temBomba) {
        this.temBomba = temBomba;
    }

    public boolean isAberta() {
        return this.aberta;
    }

    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }

    public boolean isBandeira() {
        return this.estadoMarcacao == EstadoMarcacao.BANDEIRA;
    }

    public void setBandeira(boolean bandeira) {
        this.estadoMarcacao = bandeira ? EstadoMarcacao.BANDEIRA : EstadoMarcacao.VAZIO;
    }

    public EstadoMarcacao getEstadoMarcacao() {
        return this.estadoMarcacao;
    }

    public void setEstadoMarcacao(EstadoMarcacao estadoMarcacao) {
        this.estadoMarcacao = estadoMarcacao;
    }

    public boolean isInterrogacao() {
        return this.estadoMarcacao == EstadoMarcacao.INTERROGACAO;
    }

    public int getMinasAoRedor() {
        return this.minasAoRedor;
    }

    public void setMinasAoRedor(int minasAoRedor) {
        this.minasAoRedor = minasAoRedor;
    }
}
