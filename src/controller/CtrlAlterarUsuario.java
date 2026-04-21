package controller;

import model.ModelException;
import model.Usuario;
import model.dao.DaoUsuario;
import viewer.JanelaUsuario;

public class CtrlAlterarUsuario extends CtrlAbstrato {
	private JanelaUsuario janela;
	private Usuario usuarioSelecionado;

	public CtrlAlterarUsuario(CtrlAbstrato ctrlPai, Usuario usuarioSelecionado) {
		super(ctrlPai);
		this.usuarioSelecionado = usuarioSelecionado;
		this.janela = new JanelaUsuario(this, true);
		this.janela.preencherCampos(usuarioSelecionado);
		this.janela.setVisible(true);
	}

	public void alterarUsuario(String novoNome, String novoLogin, String novaSenha) {
		try {
			DaoUsuario dao = new DaoUsuario();
			Usuario existente = dao.obterUsuarioPeloNome(novoNome);
			if (existente != null && existente != this.usuarioSelecionado) {
				this.janela.notificar("Já existe um usuário com o nome " + novoNome + "!");
				return;
			}
			existente = dao.obterUsuarioPeloLogin(novoLogin);
			if (existente != null && existente != this.usuarioSelecionado) {
				this.janela.notificar("Já existe um usuário com o login " + novoLogin + "!");
				return;
			}
			this.usuarioSelecionado.setNome(novoNome);
			this.usuarioSelecionado.setLogin(novoLogin);
			if (novaSenha != null && !novaSenha.isEmpty()) {
				this.usuarioSelecionado.setSenha(novaSenha);
			}
			dao.alterar(this.usuarioSelecionado);
			this.janela.notificar("Usuário alterado com sucesso!");
			this.encerrar();
		} catch (ModelException e) {
			this.janela.notificar(e.getMessage());
		}
	}

	public void encerrar() {
		this.janela.setVisible(false);
		if (getCtrlPai() instanceof CtrlConsultarUsuarios ctrl) {
			ctrl.fimAlterarUsuario();
		}
	}

	@Override
	public Object getBemTangivel() {
		return null;
	}
}
