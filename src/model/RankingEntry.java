package model;

public class RankingEntry {
    private Usuario usuario;
    private int melhorTempo;
    private int partidasNoPeriodo;

    public RankingEntry(Usuario usuario, int melhorTempo, int partidasNoPeriodo) {
        this.usuario = usuario;
        this.melhorTempo = melhorTempo;
        this.partidasNoPeriodo = partidasNoPeriodo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getMelhorTempo() {
        return melhorTempo;
    }

    public void setMelhorTempo(int melhorTempo) {
        this.melhorTempo = melhorTempo;
    }

    public int getPartidasNoPeriodo() {
        return partidasNoPeriodo;
    }

    public void setPartidasNoPeriodo(int partidasNoPeriodo) {
        this.partidasNoPeriodo = partidasNoPeriodo;
    }
}
