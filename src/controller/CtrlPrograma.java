package controller;

import model.Usuario;
import model.dao.Serializador;
import viewer.JanelaPrincipal;

public class CtrlPrograma extends CtrlAbstrato {
    private JanelaPrincipal janela;
    private CtrlConsultarPartidas ctrlConsultarPartidas;
    private Usuario usuarioLogado;

    public CtrlPrograma() {
        super(null);
        Serializador.recuperarObjetos();
        this.janela = new JanelaPrincipal(this);
        this.janela.setVisible(true);
    }

    public Object getBemTangivel() {
        return null;
    }

    public void encerrar() {
        this.janela.notificar("Encerrando a execução do sistema");
        this.janela.setVisible(false);
        Serializador.salvarObjetos();
        System.exit(0);
    }

    public void iniciarLogin() {
        new CtrlLogin(this);
    }

    public void iniciarConsultarUsuarios() {
        new CtrlConsultarUsuarios(this);
    }

    public void iniciarConsultarPartidas() {
        this.ctrlConsultarPartidas = new CtrlConsultarPartidas(this);
    }

    public void fimConsultarPartidas() {
        this.ctrlConsultarPartidas = null;
    }

    public CtrlConsultarPartidas getCtrlConsultarPartidas() {
        return this.ctrlConsultarPartidas;
    }

    public void fimLogin(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.janela.atualizarUsuarioLogado(usuario);
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    public static void main(String[] args) {
        new CtrlPrograma();
    }
}
