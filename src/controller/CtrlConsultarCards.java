package controller;

import model.Card;
import model.dao.DaoCard;
import viewer.JanelaConsultarCards;

public class CtrlConsultarCards extends CtrlAbstrato {
	private JanelaConsultarCards janela;
	private CtrlIncluirCard ctrlIncluirCard;
	private CtrlAlterarCard ctrlAlterarCard;

	public CtrlConsultarCards(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		DaoCard dao = new DaoCard();
		Card[] conjCards = dao.obterTodos();
		this.janela = new JanelaConsultarCards(this, conjCards);
		this.janela.setVisible(true);
	}

	public void iniciarIncluirCard() {
		this.ctrlIncluirCard = new CtrlIncluirCard(this);
	}

	public void fimIncluirCard() {
		this.ctrlIncluirCard = null;
		Card[] conjCards = new DaoCard().obterTodos();
		this.janela.atualizarDados(conjCards);
	}

	public void iniciarAlterarCard(Card c) {
		this.ctrlAlterarCard = new CtrlAlterarCard(this, c);
	}

	public void fimAlterarCard() {
		this.ctrlAlterarCard = null;
		Card[] conjCards = new DaoCard().obterTodos();
		this.janela.atualizarDados(conjCards);
	}

	public void excluirCard(Card c) {
		DaoCard dao = new DaoCard();
		dao.remover(c);
		Card[] conjCards = dao.obterTodos();
		this.janela.atualizarDados(conjCards);
		this.janela.notificar("Carta excluída com sucesso!");
	}

	public void atualizarDados() {
		Card[] conjCards = new DaoCard().obterTodos();
		this.janela.atualizarDados(conjCards);
	}

	@Override
	public void encerrar() {
		this.janela.setVisible(false);
		CtrlPrograma ctrl = (CtrlPrograma) getCtrlPai();
		ctrl.fimConsultarCards();
	}

	@Override
	public Object getBemTangivel() {
		DaoCard dao = new DaoCard();
		return dao.obterTodos();
	}
}
