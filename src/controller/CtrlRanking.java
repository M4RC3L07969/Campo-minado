package controller;

import model.PeriodoRanking;
import model.RankingService;
import model.Usuario;
import viewer.JanelaRanking;

public class CtrlRanking extends CtrlAbstrato {
	private JanelaRanking janela;
	private RankingService rankingService;

	public CtrlRanking(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		this.rankingService = new RankingService();
		Usuario usuarioLogado = null;
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			usuarioLogado = ctrl.getUsuarioLogado();
		}
		this.janela = new JanelaRanking(this, rankingService, usuarioLogado);
		this.janela.setVisible(true);
	}

	public void atualizarDados() {
		this.janela.atualizarDados();
	}

	@Override
	public void encerrar() {
		this.janela.setVisible(false);
		CtrlAbstrato ctrlPai = this.getCtrlPai();
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			ctrl.fimRanking();
		}
	}

	@Override
	public Object getBemTangivel() {
		return rankingService.obterRanking(model.jogo.Dificuldade.FACIL, PeriodoRanking.TOTAL);
	}
}
