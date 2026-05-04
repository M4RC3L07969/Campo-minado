package viewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.CtrlRanking;
import model.PeriodoRanking;
import model.RankingEntry;
import model.RankingService;
import model.Usuario;
import model.jogo.Dificuldade;

public class JanelaRanking extends JanelaAbstrata {

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JComboBox<PeriodoRanking> comboPeriodo;
	private JLabel lblContagem;
	private RankingService rankingService;
	private Usuario usuarioLogado;
	private JLabel lblSeuTempo;
	private PeriodoRanking periodoSelecionado;
	private Timer timerContagem;

	public JanelaRanking(CtrlRanking ctrl, RankingService rankingService, Usuario usuarioLogado) {
		super(ctrl);
		this.rankingService = rankingService;
		this.usuarioLogado = usuarioLogado;
		this.periodoSelecionado = PeriodoRanking.SEMANAL;

		setTitle("Ranking - Melhores Tempos");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 520, 480);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Ranking dos Jogadores");
		lblTitulo.setFont(new Font("Calibri", Font.BOLD, 18));
		lblTitulo.setBounds(140, 5, 250, 25);
		contentPane.add(lblTitulo);

		JLabel lblPeriodo = new JLabel("Período:");
		lblPeriodo.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblPeriodo.setBounds(10, 35, 50, 20);
		contentPane.add(lblPeriodo);

		comboPeriodo = new JComboBox<>(PeriodoRanking.values());
		comboPeriodo.setFont(new Font("Calibri", Font.PLAIN, 12));
		comboPeriodo.setBounds(65, 35, 100, 20);
		comboPeriodo.setSelectedItem(PeriodoRanking.SEMANAL);
		comboPeriodo.addActionListener(e -> {
			periodoSelecionado = (PeriodoRanking) comboPeriodo.getSelectedItem();
			atualizarContagem();
			atualizarDados();
		});
		contentPane.add(comboPeriodo);

		lblContagem = new JLabel();
		lblContagem.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblContagem.setBounds(175, 35, 200, 20);
		contentPane.add(lblContagem);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(10, 60, 484, 285);

		for (Dificuldade d : Dificuldade.values()) {
			JPanel painel = criarAbaRanking(d);
			tabbedPane.addTab(d.getLabel(), painel);
		}
		contentPane.add(tabbedPane);

		if (usuarioLogado != null) {
			lblSeuTempo = new JLabel();
			lblSeuTempo.setFont(new Font("Calibri", Font.PLAIN, 12));
			lblSeuTempo.setBounds(10, 350, 484, 40);
			atualizarSeuMelhorTempo();
			contentPane.add(lblSeuTempo);
		}

		JButton btFechar = new JButton("Fechar");
		btFechar.addActionListener(e -> {
			if (timerContagem != null) {
				timerContagem.stop();
			}
			getCtrl().encerrar();
		});
		btFechar.setBounds(200, 400, 100, 25);
		contentPane.add(btFechar);

		timerContagem = new Timer(60000, e -> atualizarContagem());
		timerContagem.start();
		atualizarContagem();
	}

	private void atualizarContagem() {
		if (periodoSelecionado == PeriodoRanking.TOTAL) {
			lblContagem.setText("");
			return;
		}

		LocalDateTime proximoReinicio = periodoSelecionado.getProximoReinicio();
		if (proximoReinicio == null) {
			lblContagem.setText("");
			return;
		}

		Duration duracao = Duration.between(LocalDateTime.now(), proximoReinicio);
		long minutosRestantes = duracao.toMinutes();

		if (minutosRestantes <= 0) {
			lblContagem.setText("Reiniciando...");
		} else {
			lblContagem.setText("Tempo restante: " + formatarTempoRestante(duracao));
		}
	}

	private String formatarTempoRestante(Duration d) {
		long dias = d.toDays();
		long horas = d.toHours() % 24;
		long minutos = d.toMinutes() % 60;

		if (dias > 0) {
			return dias + "d " + horas + "h";
		} else if (horas > 0) {
			return horas + "h " + minutos + "m";
		} else {
			return minutos + "m";
		}
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
		atualizarContagem();
		atualizarSeuMelhorTempo();
	}

	private JPanel criarAbaRanking(Dificuldade dificuldade) {
		JPanel painel = new JPanel();
		painel.setLayout(null);

		List<RankingEntry> ranking = rankingService.obterRanking(dificuldade, periodoSelecionado);

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
			for (RankingEntry entry : ranking) {
				Usuario u = entry.getUsuario();
				int tempo = entry.getMelhorTempo();
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
		scrollPane.setBounds(5, 5, 465, 240);
		painel.add(scrollPane);

		return painel;
	}

	private void atualizarSeuMelhorTempo() {
		if (usuarioLogado == null || lblSeuTempo == null) {
			return;
		}
		StringBuilder sb = new StringBuilder("<html>Seu melhor tempo (" + periodoSelecionado.getLabel() + "): ");
		boolean algum = false;
		for (Dificuldade d : Dificuldade.values()) {
			int tempo = rankingService.obterMelhorTempoUsuario(usuarioLogado, d, periodoSelecionado);
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
		sb.append("</html>");
		lblSeuTempo.setText(sb.toString());
	}

	private static class DestaqueUsuarioRenderer extends DefaultTableCellRenderer {
		private final List<RankingEntry> ranking;
		private final Usuario usuarioLogado;

		DestaqueUsuarioRenderer(List<RankingEntry> ranking, Usuario usuarioLogado) {
			this.ranking = ranking;
			this.usuarioLogado = usuarioLogado;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (row < ranking.size() && ranking.get(row).getUsuario().getId() == usuarioLogado.getId()) {
				c.setFont(c.getFont().deriveFont(Font.BOLD));
				c.setForeground(new Color(0, 100, 200));
			} else if (!isSelected) {
				c.setForeground(table.getForeground());
			}
			return c;
		}
	}
}
