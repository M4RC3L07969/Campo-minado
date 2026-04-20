package controller;

import model.Card;
import model.ModelException;
import model.dao.DaoCard;
import viewer.JanelaCard;

public class CtrlAlterarCard extends CtrlAbstrato {
	private JanelaCard janela;
	private Card cardSelecionado;

	public CtrlAlterarCard(CtrlAbstrato ctrlPai, Card cardSelecionado) {
		super(ctrlPai);
		this.cardSelecionado = cardSelecionado;
		this.janela = new JanelaCard(this);
		this.janela.preencherCampos(cardSelecionado);
		this.janela.setVisible(true);
	}

	public void alterarCard(String novoNome, int novaVelocidade, int novaForca, int novaInteligencia, int novaHabilidade, byte[] novaImagem) {
		try {
			this.cardSelecionado.setNome(novoNome);
			this.cardSelecionado.setVelocidade(novaVelocidade);
			this.cardSelecionado.setForca(novaForca);
			this.cardSelecionado.setInteligencia(novaInteligencia);
			this.cardSelecionado.setHabilidade(novaHabilidade);
			this.cardSelecionado.setImagem(novaImagem);
			DaoCard dao = new DaoCard();
			dao.alterar(this.cardSelecionado);
			this.janela.notificar("Carta alterada com sucesso!");
			this.encerrar();
		} catch (ModelException e) {
			this.janela.notificar(e.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);
		if (getCtrlPai() instanceof CtrlConsultarCards ctrl) {
			ctrl.fimAlterarCard();
		}
	}

	@Override
	public Object getBemTangivel() {
		return null;
	}
}
