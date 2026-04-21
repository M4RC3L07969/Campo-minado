package controller;

import model.ModelException;
import model.Usuario;
import model.dao.DaoUsuario;
import viewer.JanelaUsuario;

public class CtrlIncluirUsuario extends CtrlAbstrato {
	private JanelaUsuario janela;
	private Usuario usuarioCriado;

	public CtrlIncluirUsuario(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		this.janela = new JanelaUsuario(this);
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
		} catch (ModelException me) {
			this.janela.notificar(me.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);
		CtrlAbstrato ctrlPai = this.getCtrlPai();
		if (ctrlPai instanceof CtrlPrograma ctrl) {
			ctrl.fimIncluirUsuario();
		} else if (ctrlPai instanceof CtrlConsultarUsuarios ctrl) {
			ctrl.fimIncluirUsuario();
		}
	}

	public Object getBemTangivel() {
		return this.usuarioCriado;
	}
}
