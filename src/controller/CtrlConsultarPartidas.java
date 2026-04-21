package controller;

import model.Partida;
import model.dao.DaoPartida;
import viewer.JanelaConsultarPartidas;

public class CtrlConsultarPartidas extends CtrlAbstrato {
	private JanelaConsultarPartidas janela;

	public CtrlConsultarPartidas(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		Partida[] conjPartidas = DaoPartida.obterTodos();
		this.janela = new JanelaConsultarPartidas(this, conjPartidas);
		this.janela.setVisible(true);
	}

	public void iniciarIncluirPartida() {
		new CtrlIncluirPartida(this);
	}

	public void fimIncluirPartida() {
		Partida[] conjPartidas = DaoPartida.obterTodos();
		this.janela.atualizarDados(conjPartidas);
	}

	public void excluirPartida(Partida p) {
		DaoPartida dao = new DaoPartida();
		dao.remover(p);
		Partida[] conjPartidas = DaoPartida.obterTodos();
		this.janela.atualizarDados(conjPartidas);
		this.janela.notificar("Partida excluída com sucesso!");
	}

	public void atualizarDados() {
		Partida[] conjPartidas = DaoPartida.obterTodos();
		this.janela.atualizarDados(conjPartidas);
	}

	@Override
	public void encerrar() {
		this.janela.setVisible(false);
		CtrlAbstrato ctrlPai = this.getCtrlPai();
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			ctrl.fimConsultarPartidas();
		}
	}

	@Override
	public Object getBemTangivel() {
		return DaoPartida.obterTodos();
	}
}
