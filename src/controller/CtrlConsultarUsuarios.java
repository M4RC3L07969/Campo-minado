package controller;

import model.Usuario;
import model.dao.DaoUsuario;
import viewer.JanelaConsultarUsuarios;

public class CtrlConsultarUsuarios extends CtrlAbstrato {
	private JanelaConsultarUsuarios janela;
	private CtrlAlterarUsuario ctrlAlterarUsuario;

	public CtrlConsultarUsuarios(CtrlAbstrato ctrlPai) {
		super(ctrlPai);
		DaoUsuario dao = new DaoUsuario();
		Usuario[] conjUsuarios = dao.obterTodos();
		this.janela = new JanelaConsultarUsuarios(this, conjUsuarios);
		this.janela.setVisible(true);
	}

	public void iniciarAlterarUsuario(Usuario u) {
		this.ctrlAlterarUsuario = new CtrlAlterarUsuario(this, u);
	}

	public void fimAlterarUsuario() {
		this.ctrlAlterarUsuario = null;
		Usuario[] conjUsuarios = new DaoUsuario().obterTodos();
		this.janela.atualizarDados(conjUsuarios);
	}

	public void excluirUsuario(Usuario u) {
		DaoUsuario dao = new DaoUsuario();
		dao.remover(u);
		Usuario[] conjUsuarios = dao.obterTodos();
		this.janela.atualizarDados(conjUsuarios);
		this.janela.notificar("Usuário excluído com sucesso!");
	}

	public void atualizarDados() {
		Usuario[] conjUsuarios = new DaoUsuario().obterTodos();
		this.janela.atualizarDados(conjUsuarios);
	}

	@Override
	public void encerrar() {
		this.janela.setVisible(false);
	}

	@Override
	public Object getBemTangivel() {
		DaoUsuario dao = new DaoUsuario();
		return dao.obterTodos();
	}
}
