package viewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.CtrlAbstrato;
import controller.CtrlIncluirPartida;
import model.Usuario;
import model.dao.DaoUsuario;

public class JanelaPartida extends JanelaAbstrata {
	private JPanel contentPane;
	private JComboBox<Usuario> cbUsuario;
	private Usuario usuarioLogado;
	private JComboBox<String> cbModo;
	private JTextField tfTempo;
	private JComboBox<String> cbResultado;
	private JTextField tfDataPartida;

	public JanelaPartida(CtrlAbstrato ctrl) {
		this(ctrl, null);
	}

	public JanelaPartida(CtrlAbstrato ctrl, Usuario usuarioLogado) {
		super(ctrl);
		this.usuarioLogado = usuarioLogado;
		setTitle("Partida");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuário:");
		lblUsuario.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblUsuario.setBounds(33, 30, 80, 14);
		contentPane.add(lblUsuario);

		if (this.usuarioLogado != null) {
			JLabel lblUsuarioLogado = new JLabel(this.usuarioLogado.getNome());
			lblUsuarioLogado.setFont(new Font("Calibri", Font.PLAIN, 14));
			lblUsuarioLogado.setBounds(120, 25, 200, 20);
			contentPane.add(lblUsuarioLogado);
		} else {
			Usuario[] usuarios = DaoUsuario.obterTodos();
			cbUsuario = new JComboBox<Usuario>(usuarios);
			cbUsuario.setBounds(120, 25, 200, 20);
			contentPane.add(cbUsuario);
		}

		JLabel lblModo = new JLabel("Modo:");
		lblModo.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblModo.setBounds(33, 60, 80, 14);
		contentPane.add(lblModo);

		String[] modos = { "Fácil (8x8)", "Médio (12x12)", "Difícil (16x16)" };
		cbModo = new JComboBox<String>(modos);
		cbModo.setBounds(120, 55, 200, 20);
		contentPane.add(cbModo);

		JLabel lblTempo = new JLabel("Tempo (s):");
		lblTempo.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblTempo.setBounds(33, 90, 80, 14);
		contentPane.add(lblTempo);

		tfTempo = new JTextField();
		tfTempo.setBounds(120, 85, 200, 20);
		contentPane.add(tfTempo);
		tfTempo.setColumns(10);

		JLabel lblResultado = new JLabel("Resultado:");
		lblResultado.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblResultado.setBounds(33, 120, 80, 14);
		contentPane.add(lblResultado);

		String[] resultados = { "Vitória", "Derrota" };
		cbResultado = new JComboBox<String>(resultados);
		cbResultado.setBounds(120, 115, 200, 20);
		contentPane.add(cbResultado);

		JLabel lblDataPartida = new JLabel("Data:");
		lblDataPartida.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblDataPartida.setBounds(33, 150, 80, 14);
		contentPane.add(lblDataPartida);

		tfDataPartida = new JTextField();
		tfDataPartida.setBounds(120, 145, 200, 20);
		contentPane.add(tfDataPartida);
		tfDataPartida.setColumns(10);

		JButton btOk = new JButton("Ok");
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Usuario usuario = usuarioLogado;
					if (usuario == null) {
						usuario = (Usuario) cbUsuario.getSelectedItem();
					}
					String modo = (String) cbModo.getSelectedItem();
					int tempo = Integer.parseInt(tfTempo.getText());
					String resultado = (String) cbResultado.getSelectedItem();
					String dataPartida = tfDataPartida.getText();

					if (getCtrl() instanceof CtrlIncluirPartida ctrl) {
						ctrl.incluirPartida(usuario, modo, tempo, resultado, dataPartida);
					} else {
						JOptionPane.showMessageDialog(btOk, "Controlador inválido.");
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(btOk, "Tempo deve ser um número inteiro.");
				}
			}
		});
		btOk.setBounds(70, 190, 89, 23);
		contentPane.add(btOk);

		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btCancelar.setBounds(180, 190, 89, 23);
		contentPane.add(btCancelar);
	}
}
