package viewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.CtrlAbstrato;
import controller.CtrlIncluirUsuario;
import controller.CtrlAlterarUsuario;
import model.Usuario;

public class JanelaUsuario extends JanelaAbstrata {
	private JPanel contentPane;
	private JTextField tfNome;
	private JPasswordField pfSenha;
	private boolean senhaVisivel = false;
	private boolean modoEdicao = false;

	public JanelaUsuario(CtrlAbstrato ctrl) {
		this(ctrl, false);
	}

	public JanelaUsuario(CtrlAbstrato ctrl, boolean modoEdicao) {
		super(ctrl);
		this.modoEdicao = modoEdicao;
		setTitle("Usuário");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblNome.setBounds(33, 30, 60, 14);
		contentPane.add(lblNome);

		tfNome = new JTextField();
		tfNome.setBounds(100, 25, 200, 20);
		contentPane.add(tfNome);
		tfNome.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblSenha.setBounds(33, 65, 60, 14);
		contentPane.add(lblSenha);

		pfSenha = new JPasswordField();
		pfSenha.setBounds(100, 60, 170, 20);
		contentPane.add(pfSenha);

		JButton btOlho = new JButton("👁");
		btOlho.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
		btOlho.setBounds(275, 60, 30, 20);
		btOlho.addActionListener(e -> {
			senhaVisivel = !senhaVisivel;
			if (senhaVisivel) {
				pfSenha.setEchoChar((char) 0);
				btOlho.setText("🙈");
			} else {
				pfSenha.setEchoChar('•');
				btOlho.setText("👁");
			}
		});
		contentPane.add(btOlho);

		if (modoEdicao) {
			lblSenha.setVisible(false);
			pfSenha.setVisible(false);
			btOlho.setVisible(false);
		}

		JButton btOk = new JButton("Ok");
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = tfNome.getText();
				String senha = modoEdicao ? null : new String(pfSenha.getPassword());

				if (getCtrl() instanceof CtrlIncluirUsuario ctrl) {
					ctrl.incluirUsuario(nome, senha);
				} else if (getCtrl() instanceof CtrlAlterarUsuario ctrl) {
					ctrl.alterarUsuario(nome, senha);
				} else {
					JOptionPane.showMessageDialog(btOk, "Controlador inválido.");
				}
			}
		});
		btOk.setBounds(70, 110, 89, 23);
		contentPane.add(btOk);

		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btCancelar.setBounds(180, 110, 89, 23);
		contentPane.add(btCancelar);
	}

	public void preencherCampos(Usuario u) {
		tfNome.setText(u.getNome());
		pfSenha.setText("");
	}
}
