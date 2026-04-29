package viewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.CtrlRanking;
import model.RankingService;
import model.Usuario;
import model.jogo.Dificuldade;

public class JanelaRanking extends JanelaAbstrata {

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private RankingService rankingService;
	private Usuario usuarioLogado;
	private JLabel lblSeuTempo;

	public JanelaRanking(CtrlRanking ctrl, RankingService rankingService, Usuario usuarioLogado) {
		super(ctrl);
		this.rankingService = rankingService;
		this.usuarioLogado = usuarioLogado;

		setTitle("Ranking - Melhores Tempos");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 520, 460);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("🏆 Ranking dos Jogadores");
		lblTitulo.setFont(new Font("Calibri", Font.BOLD, 18));
		lblTitulo.setBounds(140, 5, 250, 25);
		contentPane.add(lblTitulo);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(10, 35, 484, 310);

		for (Dificuldade d : Dificuldade.values()) {
			JPanel painel = criarAbaRanking(d);
			tabbedPane.addTab(d.getLabel(), painel);
		}
		contentPane.add(tabbedPane);

		if (usuarioLogado != null) {
			lblSeuTempo = new JLabel();
			lblSeuTempo.setFont(new Font("Calibri", Font.PLAIN, 12));
			lblSeuTempo.setBounds(10, 350, 484, 20);
			atualizarSeuMelhorTempo();
			contentPane.add(lblSeuTempo);
		}

		JButton btFechar = new JButton("Fechar");
		btFechar.addActionListener(e -> getCtrl().encerrar());
		btFechar.setBounds(200, 380, 100, 25);
		contentPane.add(btFechar);
	}

	public void atualizarDados() {
		int abaSelecionada = tabbedPane.getSelectedIndex();
		tabbedPane.removeAll();
		for (Dificuldade d : Dificuldade.values()) {
			JPanel painel = criarAbaRanking(d);
			tabbedPane.addTab(d.getLabel(), painel);
		}
		if (abaSelecionada >= 0 && abaSelecionada < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(abaSelecionada);
		}
		atualizarSeuMelhorTempo();
	}

	private JPanel criarAbaRanking(Dificuldade dificuldade) {
		JPanel painel = new JPanel();
		painel.setLayout(null);

		List<Usuario> ranking = rankingService.obterRanking(dificuldade);

		String[] colunas = { "🏆", "Jogador", "Tempo" };
		DefaultTableModel model = new DefaultTableModel(colunas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		if (ranking.isEmpty()) {
			model.addRow(new Object[] { "", "Nenhum jogador", "" });
		} else {
			int pos = 1;
			for (Usuario u : ranking) {
				int tempo = rankingService.obterTempo(u, dificuldade);
				String nomeExibicao = u.getNome();
				if (usuarioLogado != null && u.getId() == usuarioLogado.getId()) {
					nomeExibicao += " ← você";
				}
				model.addRow(new Object[] { pos, nomeExibicao,
						RankingService.formatarTempo(tempo) });
				pos++;
			}
		}

		JTable tabela = new JTable(model);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(220);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(80);
		tabela.setRowHeight(40);
		tabela.setFont(new Font("Calibri", Font.PLAIN, 13));

		tabela.getColumnModel().getColumn(0).setCellRenderer(new MedalhaRenderer(ranking, usuarioLogado));

		if (usuarioLogado != null) {
			tabela.setDefaultRenderer(Object.class, new DestaqueUsuarioRenderer(ranking, usuarioLogado));
		}

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setBounds(5, 5, 465, 265);
		painel.add(scrollPane);

		return painel;
	}

	private void atualizarSeuMelhorTempo() {
		if (usuarioLogado == null || lblSeuTempo == null) {
			return;
		}
		StringBuilder sb = new StringBuilder("Seu melhor tempo — ");
		boolean algum = false;
		for (Dificuldade d : Dificuldade.values()) {
			int tempo = rankingService.obterTempo(usuarioLogado, d);
			if (tempo > 0) {
				if (algum)
					sb.append(" | ");
				sb.append(d.getLabel()).append(": ").append(RankingService.formatarTempo(tempo));
				algum = true;
			}
		}
		if (!algum) {
			sb.append("Nenhum tempo registrado");
		}
		lblSeuTempo.setText(sb.toString());
	}

	private static class DestaqueUsuarioRenderer extends DefaultTableCellRenderer {
		private final List<Usuario> ranking;
		private final Usuario usuarioLogado;

		DestaqueUsuarioRenderer(List<Usuario> ranking, Usuario usuarioLogado) {
			this.ranking = ranking;
			this.usuarioLogado = usuarioLogado;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (row < ranking.size() && ranking.get(row).getId() == usuarioLogado.getId()) {
				c.setFont(c.getFont().deriveFont(Font.BOLD));
				c.setForeground(new Color(0, 100, 200));
			} else if (!isSelected) {
				c.setForeground(table.getForeground());
			}
			return c;
		}
	}
}
