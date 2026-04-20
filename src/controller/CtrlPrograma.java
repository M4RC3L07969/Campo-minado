package controller;

import model.dao.Serializador;
import viewer.JanelaPrincipal;

public class CtrlPrograma extends CtrlAbstrato {
    //
    // ATRIBUTOS
    //
    private JanelaPrincipal janela;
    private CtrlIncluirCard ctrlIncluirCard;
    private CtrlConsultarCards ctrlConsultarCards;

    //
    // MÉTODOS
    //
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

    public void iniciarIncluirCard() {
        this.ctrlIncluirCard = new CtrlIncluirCard(this);
    }

    public void fimIncluirCard() {
        this.ctrlIncluirCard = null;
        if (this.ctrlConsultarCards != null)
            this.ctrlConsultarCards.atualizarDados();
    }

    public void iniciarConsultarCards() {
        this.ctrlConsultarCards = new CtrlConsultarCards(this);
    }

    public void fimConsultarCards() {
        this.ctrlConsultarCards = null;
    }

    public static void main(String[] args) {
        new CtrlPrograma();
    }
}
