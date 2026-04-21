package viewer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import controller.CtrlConsultarUsuarios;
import model.Usuario;

public class JanelaConsultarUsuarios extends JanelaAbstrata {
	private JPanel contentPane;
	private JTable tabela;
	private Usuario[] listaUsuarios;

	public JanelaConsultarUsuarios(CtrlConsultarUsuarios ctrl, Usuario[] conjUsuarios) {
		super(ctrl);
		setTitle("Usuários");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.atualizarDados(conjUsuarios);

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setBounds(10, 11, 564, 250);
		contentPane.add(scrollPane);

		JButton btExcluir = new JButton("Excluir");
		btExcluir.addActionListener(e -> {
			Usuario u = obterLinhaSelecionada();
			if (u != null) {
				int confirmacao = JOptionPane.showConfirmDialog(
						this,
						"Você tem certeza que deseja deletar o usuário " + u.getNome()
								+ "?\nTodas as partidas relacionadas a este usuário também serão excluídas.",
						"Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
				if (confirmacao == JOptionPane.YES_OPTION) {
					CtrlConsultarUsuarios ctrl2 = (CtrlConsultarUsuarios) getCtrl();
					ctrl2.excluirUsuario(u);
				}
			} else {
				notificar("Selecione um usuário para exclusão");
			}
		});
		btExcluir.setBounds(10, 270, 89, 23);
		contentPane.add(btExcluir);

		JButton btAlterar = new JButton("Alterar");
		btAlterar.addActionListener(e -> {
			Usuario u = obterLinhaSelecionada();
			if (u != null) {
				CtrlConsultarUsuarios ctrl2 = (CtrlConsultarUsuarios) getCtrl();
				ctrl2.iniciarAlterarUsuario(u);
			} else {
				notificar("Selecione um usuário para alteração");
			}
		});
		btAlterar.setBounds(230, 270, 89, 23);
		contentPane.add(btAlterar);

		JButton btSair = new JButton("Sair");
		btSair.addActionListener(e -> getCtrl().encerrar());
		btSair.setBounds(485, 270, 89, 23);
		contentPane.add(btSair);

		this.setVisible(true);
	}

	public void atualizarDados(Usuario[] conjUsuarios) {
		this.listaUsuarios = conjUsuarios;
		HelperTableModel h = new HelperTableModel(this.listaUsuarios);
		if (this.tabela == null)
			this.tabela = new JTable(h.getTableModel());
		else
			this.tabela.setModel(h.getTableModel());
	}

	public Usuario obterLinhaSelecionada() {
		if (this.listaUsuarios == null || this.listaUsuarios.length == 0)
			return null;
		int numLinhaSelecionada = this.tabela.getSelectedRow();
		if (numLinhaSelecionada != -1 && numLinhaSelecionada < this.listaUsuarios.length)
			return this.listaUsuarios[numLinhaSelecionada];
		return null;
	}
}
