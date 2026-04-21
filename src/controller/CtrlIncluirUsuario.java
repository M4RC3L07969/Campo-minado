package controller;

import model.ModelException;
import model.Usuario;
import model.dao.DaoUsuario;
import viewer.JanelaUsuario;

public class CtrlIncluirUsuario extends CtrlAbstrato {
	private JanelaUsuario janela;
	private Usuario usuarioCriado;

	public CtrlIncluirUsuario(CtrlAbstrato ctrlPai) {
		this(ctrlPai, null, null);
	}

	public CtrlIncluirUsuario(CtrlAbstrato ctrlPai, String nomeInicial, String senhaInicial) {
		super(ctrlPai);
		this.janela = new JanelaUsuario(this, false, nomeInicial, senhaInicial);
		this.janela.setVisible(true);
	}

	public void incluirUsuario(String nome, String senha) {
		try {
			this.usuarioCriado = new Usuario(nome, senha);
			DaoUsuario dao = new DaoUsuario();
			boolean incluido = dao.incluir(this.usuarioCriado);
			if (!incluido) {
				this.janela.notificar("Já existe um usuário com o nome " + nome + "!");
				return;
			}
			this.janela.notificar("Usuário " + nome + " incluído com sucesso!");
			this.encerrar();

			// Se foi chamado pelo CtrlPrograma (Sign In), já loga o usuário
			CtrlAbstrato ctrlPai = this.getCtrlPai();
			if (ctrlPai instanceof CtrlPrograma ctrl) {
				ctrl.fimLogin(this.usuarioCriado);
			}
		} catch (ModelException me) {
			this.janela.notificar(me.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);
	}

	public Object getBemTangivel() {
		return this.usuarioCriado;
	}
}
