package controller;

import model.Partida;
import model.Usuario;
import model.dao.DaoPartida;
import viewer.JanelaPartida;

public class CtrlIncluirPartida extends CtrlAbstrato {
	private JanelaPartida janela;
	private Partida partidaCriada;

	public CtrlIncluirPartida(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		Usuario usuarioLogado = null;
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			usuarioLogado = ctrl.getUsuarioLogado();
		}
		this.janela = new JanelaPartida(this, usuarioLogado);
		this.janela.setVisible(true);
	}

	public void incluirPartida(Usuario usuario, String modo, int tempo, String resultado, String dataPartida) {
		try {
			String modoSimplificado = modo.substring(modo.indexOf("(") + 1, modo.indexOf(")"));

			String resultadoSimplificado = resultado.replace("ó", "o");

			this.partidaCriada = new Partida(usuario, modoSimplificado, tempo, resultadoSimplificado, dataPartida);
			DaoPartida dao = new DaoPartida();
			boolean incluido = dao.incluir(this.partidaCriada);
			if (!incluido) {
				this.janela.notificar("Erro ao incluir partida!");
				return;
			}
			this.janela.notificar("Partida incluída com sucesso!");
			this.encerrar();

			CtrlAbstrato ctrlPai = this.getCtrlPai();
			if (ctrlPai instanceof CtrlConsultarPartidas ctrl) {
				ctrl.fimIncluirPartida();
			} else if (ctrlPai instanceof CtrlPrograma ctrl) {
				CtrlConsultarPartidas ctrlConsultar = ctrl.getCtrlConsultarPartidas();
				if (ctrlConsultar != null) {
					ctrlConsultar.fimIncluirPartida();
				}
			}
		} catch (Exception e) {
			this.janela.notificar(e.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);
	}

	@Override
	public Object getBemTangivel() {
		return this.partidaCriada;
	}
}
