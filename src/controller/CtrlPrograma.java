package controller;

import model.Usuario;
import model.jogo.ModoJogo;
import util.ThemeManager;
import viewer.JanelaPrincipal;

public class CtrlPrograma extends CtrlAbstrato {
    private JanelaPrincipal janela;
    private CtrlConsultarPartidas ctrlConsultarPartidas;
    private Usuario usuarioLogado;
    private CtrlCampoMinado ctrlCampoMinado;

    public CtrlPrograma() {
        super(null);
        this.janela = new JanelaPrincipal(this);
        this.janela.setVisible(true);
    }

    public Object getBemTangivel() {
        return null;
    }

    public void encerrar() {
        this.janela.notificar("Encerrando a execução do sistema");
        this.janela.setVisible(false);
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

    public void iniciarJogo(ModoJogo modo) {
        this.ctrlCampoMinado = new CtrlCampoMinado(this, modo);
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

        if (this.ctrlCampoMinado != null) {
            this.ctrlCampoMinado.tentarSalvarPartidaAposLogin();
        }
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    public void setCtrlCampoMinado(CtrlCampoMinado ctrl) {
        this.ctrlCampoMinado = ctrl;
    }

    public static void main(String[] args) {
        ThemeManager.getInstance().applyTheme();
        new CtrlPrograma();
    }
}
