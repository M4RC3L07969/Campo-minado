package controller;

import model.dao.Serializador;
import viewer.JanelaPrincipal;

public class CtrlPrograma extends CtrlAbstrato {
    private JanelaPrincipal janela;
    private CtrlIncluirUsuario ctrlIncluirUsuario;
    private CtrlConsultarUsuarios ctrlConsultarUsuarios;

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

    public void iniciarIncluirUsuario() {
        this.ctrlIncluirUsuario = new CtrlIncluirUsuario(this);
    }

    public void fimIncluirUsuario() {
        this.ctrlIncluirUsuario = null;
        if (this.ctrlConsultarUsuarios != null)
            this.ctrlConsultarUsuarios.atualizarDados();
    }

    public void iniciarConsultarUsuarios() {
        this.ctrlConsultarUsuarios = new CtrlConsultarUsuarios(this);
    }

    public void fimConsultarUsuarios() {
        this.ctrlConsultarUsuarios = null;
    }

    public static void main(String[] args) {
        new CtrlPrograma();
    }
}
