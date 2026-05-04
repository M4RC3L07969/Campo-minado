package viewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import javax.swing.JOptionPane;

import controller.CtrlPrograma;
import model.Usuario;
import model.jogo.ModoJogo;
import util.SvgIconUtil;
import util.ThemeManager;

public class JanelaPrincipal extends JanelaAbstrata {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUsuarioLogado;
	private JButton btPerfil;
	private JPopupMenu popupMenu;

	public JanelaPrincipal(CtrlPrograma ctrl) {
		super(ctrl);
		setTitle("Super Trunfo - Menu Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btPerfil = new JButton();
		btPerfil.setIcon(SvgIconUtil.createPerfilIcon(30));
		btPerfil.setToolTipText("Perfil");
		btPerfil.setBorderPainted(false);
		btPerfil.setContentAreaFilled(false);
		btPerfil.setFocusPainted(false);
		btPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btPerfil.putClientProperty("JButton.buttonType", "borderless");
		btPerfil.setBounds(340, 2, 35, 30);
		btPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reconstruirMenu();
				popupMenu.show(btPerfil, 0, btPerfil.getHeight());
			}
		});

		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				popupMenu.setVisible(false);
			}
		});
		contentPane.add(btPerfil);

		popupMenu = new JPopupMenu();
		reconstruirMenu();

		JButton btJogar = new JButton("Jogar");
		btJogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] opcoes = { "Fácil (9x9)", "Médio (16x16)", "Difícil (30x16)" };
				int escolha = JOptionPane.showOptionDialog(null, "Selecione a dificuldade:", "Campo Minado",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
				if (escolha == JOptionPane.CLOSED_OPTION)
					return;
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ModoJogo modo = switch (escolha) {
					case 0 -> ModoJogo.FACIL;
					case 1 -> ModoJogo.MEDIO;
					case 2 -> ModoJogo.DIFICIL;
					default -> ModoJogo.FACIL;
				};
				ctrl.iniciarJogo(modo);
			}
		});
		btJogar.setBounds(115, 85, 150, 30);
		contentPane.add(btJogar);

		JButton btConsultarUsuarios = new JButton("Consultar Usuários");
		btConsultarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarConsultarUsuarios();
			}
		});
		btConsultarUsuarios.setBounds(115, 120, 150, 30);
		contentPane.add(btConsultarUsuarios);

		JButton btConsultarPartidas = new JButton("Consultar Partidas");
		btConsultarPartidas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarConsultarPartidas();
			}
		});
		btConsultarPartidas.setBounds(115, 155, 150, 30);
		contentPane.add(btConsultarPartidas);

		JButton btRanking = new JButton("Ranking");
		btRanking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarRanking();
			}
		});
		btRanking.setBounds(115, 190, 150, 30);
		contentPane.add(btRanking);

		JButton btSair = new JButton("Sair");
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCtrl().encerrar();
			}
		});
		btSair.setBounds(130, 225, 120, 30);
		contentPane.add(btSair);
	}

	private void reconstruirMenu() {
		popupMenu.removeAll();
		CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
		Usuario usuario = ctrl.getUsuarioLogado();

		if (usuario == null) {
			JMenuItem miEntrar = new JMenuItem("Entrar");
			miEntrar.addActionListener(e -> ctrl.iniciarLogin());
			popupMenu.add(miEntrar);

			JMenuItem miCriarConta = new JMenuItem("Criar conta");
			miCriarConta.addActionListener(e -> ctrl.iniciarCadastro());
			popupMenu.add(miCriarConta);
		} else {
			JPanel painelNome = new JPanel(new BorderLayout());
			painelNome.setOpaque(false);
			JLabel lblNome = new JLabel(usuario.getNome(), SwingConstants.CENTER);
			lblNome.setFont(lblNome.getFont().deriveFont(Font.BOLD));
			lblNome.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			painelNome.add(lblNome, BorderLayout.CENTER);
			popupMenu.add(painelNome);

			popupMenu.addSeparator();

			JCheckBox checkModoClaro = new JCheckBox("Modo claro");
			checkModoClaro.setSelected(!ThemeManager.getInstance().isDarkMode());
			checkModoClaro.putClientProperty("JCheckBox.style", "switch");
			checkModoClaro.putClientProperty("JComponent.variant", "switch");
			checkModoClaro.setFocusable(false);
			checkModoClaro.addActionListener(e -> {
				ThemeManager.getInstance().toggleTheme();
			});
			popupMenu.add(checkModoClaro);

			popupMenu.addSeparator();

			JMenuItem miSair = new JMenuItem("Sair da conta");
			miSair.addActionListener(e -> ctrl.logout());
			popupMenu.add(miSair);
		}
	}

	public void atualizarUsuarioLogado(Usuario usuario) {
		reconstruirMenu();
	}
}
