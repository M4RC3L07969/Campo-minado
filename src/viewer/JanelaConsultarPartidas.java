package viewer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import controller.CtrlConsultarPartidas;
import model.Partida;

public class JanelaConsultarPartidas extends JanelaAbstrata {
	private JPanel contentPane;
	private JTable tabela;
	private Partida[] listaPartidas;

	public JanelaConsultarPartidas(CtrlConsultarPartidas ctrl, Partida[] conjPartidas) {
		super(ctrl);
		setTitle("Partidas");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.atualizarDados(conjPartidas);

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setBounds(10, 11, 564, 250);
		contentPane.add(scrollPane);

		JButton btExcluir = new JButton("Excluir");
		btExcluir.addActionListener(e -> {
			Partida p = obterLinhaSelecionada();
			if (p != null) {
				int confirmacao = JOptionPane.showConfirmDialog(
						this, "Você tem certeza que deseja deletar a partida " + p.getId() + "?",
						"Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
				if (confirmacao == JOptionPane.YES_OPTION) {
					CtrlConsultarPartidas ctrl2 = (CtrlConsultarPartidas) getCtrl();
					ctrl2.excluirPartida(p);
				}
			} else {
				notificar("Selecione uma partida para exclusão");
			}
		});
		btExcluir.setBounds(10, 270, 89, 23);
		contentPane.add(btExcluir);

		JButton btIncluir = new JButton("Incluir");
		btIncluir.addActionListener(e -> {
			CtrlConsultarPartidas ctrl2 = (CtrlConsultarPartidas) getCtrl();
			ctrl2.iniciarIncluirPartida();
		});
		btIncluir.setBounds(120, 270, 89, 23);
		contentPane.add(btIncluir);

		JButton btSair = new JButton("Sair");
		btSair.addActionListener(e -> getCtrl().encerrar());
		btSair.setBounds(485, 270, 89, 23);
		contentPane.add(btSair);

		this.setVisible(true);
	}

	public void atualizarDados(Partida[] conjPartidas) {
		this.listaPartidas = conjPartidas;
		HelperTableModel h = new HelperTableModel(this.listaPartidas);
		if (this.tabela == null)
			this.tabela = new JTable(h.getTableModel());
		else
			this.tabela.setModel(h.getTableModel());
	}

	public Partida obterLinhaSelecionada() {
		if (this.listaPartidas == null || this.listaPartidas.length == 0)
			return null;
		int numLinhaSelecionada = this.tabela.getSelectedRow();
		if (numLinhaSelecionada != -1 && numLinhaSelecionada < this.listaPartidas.length)
			return this.listaPartidas[numLinhaSelecionada];
		return null;
	}
}
