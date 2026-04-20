package viewer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import controller.CtrlConsultarCards;
import model.Card;

public class JanelaConsultarCards extends JanelaAbstrata {
	private JPanel contentPane;
	private JTable tabela;
	private Card[] listaCards;

	public JanelaConsultarCards(CtrlConsultarCards ctrl, Card[] conjCards) {
		super(ctrl);
		setTitle("Cartas");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.atualizarDados(conjCards);

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setBounds(10, 11, 564, 250);
		contentPane.add(scrollPane);

		JButton btIncluir = new JButton("Incluir");
		btIncluir.addActionListener(e -> {
			CtrlConsultarCards c = (CtrlConsultarCards) getCtrl();
			c.iniciarIncluirCard();
		});
		btIncluir.setBounds(10, 270, 89, 23);
		contentPane.add(btIncluir);

		JButton btExcluir = new JButton("Excluir");
		btExcluir.addActionListener(e -> {
			Card c = obterLinhaSelecionada();
			if (c != null) {
				int confirmacao = JOptionPane.showConfirmDialog(
						this, "Você tem certeza que deseja deletar a carta " + c.getNome() + "?",
						"Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
				if (confirmacao == JOptionPane.YES_OPTION) {
					CtrlConsultarCards ctrl2 = (CtrlConsultarCards) getCtrl();
					ctrl2.excluirCard(c);
				}
			} else {
				notificar("Selecione uma carta para exclusão");
			}
		});
		btExcluir.setBounds(120, 270, 89, 23);
		contentPane.add(btExcluir);

		JButton btAlterar = new JButton("Alterar");
		btAlterar.addActionListener(e -> {
			Card c = obterLinhaSelecionada();
			if (c != null) {
				CtrlConsultarCards ctrl2 = (CtrlConsultarCards) getCtrl();
				ctrl2.iniciarAlterarCard(c);
			} else {
				notificar("Selecione uma carta para alteração");
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

	public void atualizarDados(Card[] conjCards) {
		this.listaCards = conjCards;
		HelperTableModel h = new HelperTableModel(this.listaCards);
		if (this.tabela == null)
			this.tabela = new JTable(h.getTableModel());
		else
			this.tabela.setModel(h.getTableModel());
	}

	public Card obterLinhaSelecionada() {
		if (this.listaCards == null || this.listaCards.length == 0)
			return null;
		int numLinhaSelecionada = this.tabela.getSelectedRow();
		if (numLinhaSelecionada != -1 && numLinhaSelecionada < this.listaCards.length)
			return this.listaCards[numLinhaSelecionada];
		return null;
	}
}
