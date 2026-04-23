package viewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JOptionPane;

import controller.CtrlPrograma;
import model.Usuario;
import model.jogo.ModoJogo;

public class JanelaPrincipal extends JanelaAbstrata {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUsuarioLogado;

	public JanelaPrincipal(CtrlPrograma ctrl) {
		super(ctrl);
		setTitle("Super Trunfo - Menu Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblUsuarioLogado = new JLabel("Usuário: Não logado");
		lblUsuarioLogado.setFont(new Font("Calibri", Font.BOLD, 14));
		lblUsuarioLogado.setBounds(30, 5, 340, 20);
		contentPane.add(lblUsuarioLogado);

		JButton btLogin = new JButton("Login");
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarLogin();
			}
		});
		btLogin.setBounds(30, 35, 150, 30);
		contentPane.add(btLogin);

		JButton btSignIn = new JButton("Sign In");
		btSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new controller.CtrlIncluirUsuario(getCtrl());
			}
		});
		btSignIn.setBounds(200, 35, 150, 30);
		contentPane.add(btSignIn);

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

		JButton btIncluirPartida = new JButton("Incluir Partida");
		btIncluirPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new controller.CtrlIncluirPartida(getCtrl());
			}
		});
		btIncluirPartida.setBounds(115, 155, 150, 30);
		contentPane.add(btIncluirPartida);

		JButton btConsultarPartidas = new JButton("Consultar Partidas");
		btConsultarPartidas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarConsultarPartidas();
			}
		});
		btConsultarPartidas.setBounds(115, 190, 150, 30);
		contentPane.add(btConsultarPartidas);

		JButton btSair = new JButton("Sair");
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCtrl().encerrar();
			}
		});
		btSair.setBounds(130, 230, 120, 30);
		contentPane.add(btSair);
	}

	public void atualizarUsuarioLogado(Usuario usuario) {
		if (usuario == null) {
			lblUsuarioLogado.setText("Usuário: Não logado");
		} else {
			lblUsuarioLogado.setText("Usuário: " + usuario.getNome());
		}
	}
}
