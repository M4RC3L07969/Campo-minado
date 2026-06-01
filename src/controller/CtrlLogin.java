package controller;

import model.Usuario;
import model.dao.DaoUsuario;
import viewer.JanelaLogin;

public class CtrlLogin extends CtrlAbstrato {
	private JanelaLogin janela;
	private Usuario usuarioLogado;

	public CtrlLogin(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		this.janela = new JanelaLogin(this);
		this.janela.setVisible(true);
	}

	public void fazerLogin(String identificador, String senha) {
		try {
			DaoUsuario dao = new DaoUsuario();
			Usuario usuario = dao.obterUsuarioPeloNome(identificador);

			if (usuario == null) {
				usuario = dao.obterUsuarioPeloLogin(identificador);
			}

			if (usuario == null) {
				this.janela.notificar("Usuário não encontrado!");
				return;
			}

			if (!usuario.verificarSenha(senha)) {
				this.janela.notificar("Senha incorreta!");
				return;
			}

			this.usuarioLogado = usuario;
			this.janela.notificar("Login realizado com sucesso!");
			this.encerrar();
		} catch (Exception e) {
			this.janela.notificar("Erro ao fazer login: " + e.getMessage());
		}
	}

	public void criarConta(String nome, String senha) {
		new CtrlIncluirUsuario(this, this, nome, senha);
	}

	public void encerrar() {
		this.janela.setVisible(false);
		if (this.usuarioLogado != null) {
			CtrlAbstrato ctrlPai = this.getCtrlPai();
			if (ctrlPai instanceof CtrlPrograma ctrl) {
				ctrl.fimLogin(this.usuarioLogado);
			}
		}
	}

	public Object getBemTangivel() {
		return this.usuarioLogado;
	}
}
