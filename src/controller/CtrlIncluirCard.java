package controller;

import model.Card;
import model.ModelException;
import model.dao.DaoCard;
import viewer.JanelaCard;

public class CtrlIncluirCard extends CtrlAbstrato {
	//
	// ATRIBUTO
	//
	private JanelaCard janela;
	private Card cardCriado;

	//
	// MÉTODOS
	//

	public CtrlIncluirCard(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		this.janela = new JanelaCard(this);
		this.janela.setVisible(true);
	}

	public void incluirCard(String nome, int velocidade, int forca, int inteligencia, int habilidade, byte[] imagem) {
		try {
			this.cardCriado = new Card(nome, velocidade, forca, inteligencia, habilidade, imagem);
			DaoCard dao = new DaoCard();
			dao.incluir(this.cardCriado);
			this.janela.notificar("Carta " + nome + " incluída com sucesso!");
			this.encerrar();
		} catch (ModelException me) {
			this.janela.notificar(me.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);

		CtrlAbstrato ctrlPai = this.getCtrlPai();
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			ctrl.fimIncluirCard();
		} else if (ctrlPai instanceof CtrlConsultarCards ctrl) {
			ctrl.fimIncluirCard();
		}
	}

	public Object getBemTangivel() {
		return this.cardCriado;
	}
}
