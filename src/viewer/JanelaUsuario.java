package viewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.CtrlAbstrato;
import controller.CtrlIncluirUsuario;
import util.SvgIconUtil;
import util.JTextFieldEmail;

public class JanelaUsuario extends JanelaAbstrata {
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextFieldEmail tfLogin;
	private JPasswordField pfSenha;
	private boolean senhaVisivel = false;

	public JanelaUsuario(CtrlAbstrato ctrl) {
		this(ctrl, null, null, null);
	}

	public JanelaUsuario(CtrlAbstrato ctrl, String nomeInicial, String loginInicial, String senhaInicial) {
		super(ctrl);
		setTitle("Usuário");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 350, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Usuário:");
		lblNome.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblNome.setBounds(33, 30, 60, 14);
		contentPane.add(lblNome);

		tfNome = new JTextField();
		tfNome.setBounds(33, 50, 270, 25);
		contentPane.add(tfNome);
		tfNome.setColumns(10);

		JLabel lblLogin = new JLabel("E-mail:");
		lblLogin.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblLogin.setBounds(33, 90, 60, 14);
		contentPane.add(lblLogin);

		tfLogin = new JTextFieldEmail();
		tfLogin.setBounds(33, 110, 270, 25);
		contentPane.add(tfLogin);
		tfLogin.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblSenha.setBounds(33, 150, 60, 14);
		contentPane.add(lblSenha);

		pfSenha = new JPasswordField();
		pfSenha.setBounds(33, 170, 240, 25);
		contentPane.add(pfSenha);

		JButton btOlho = new JButton();
		btOlho.setIcon(SvgIconUtil.createEyeOpenIcon(14));
		btOlho.setBounds(280, 170, 23, 25);
		btOlho.setContentAreaFilled(false);
		btOlho.setBorderPainted(false);
		btOlho.setFocusPainted(false);
		btOlho.addActionListener(e -> {
			senhaVisivel = !senhaVisivel;
			if (senhaVisivel) {
				pfSenha.setEchoChar((char) 0);
				btOlho.setIcon(SvgIconUtil.createEyeClosedIcon(14));
			} else {
				pfSenha.setEchoChar('•');
				btOlho.setIcon(SvgIconUtil.createEyeOpenIcon(14));
			}
		});
		contentPane.add(btOlho);

		JButton btOk = new JButton("Ok");
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = tfNome.getText();
				String login = tfLogin.getRealText();
				String senha = new String(pfSenha.getPassword());

				if (!tfLogin.isEmailValid()) {
					JOptionPane.showMessageDialog(btOk,
							"Por favor, insira um e-mail válido (deve conter texto antes e depois do @).");
					return;
				}

				if (getCtrl() instanceof CtrlIncluirUsuario ctrl) {
					ctrl.incluirUsuario(nome, login, senha);
				} else {
					JOptionPane.showMessageDialog(btOk, "Controlador inválido.");
				}
			}
		});
		btOk.setBounds(33, 215, 130, 30);
		contentPane.add(btOk);

		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btCancelar.setBounds(173, 215, 130, 30);
		contentPane.add(btCancelar);

		if (nomeInicial != null) {
			tfNome.setText(nomeInicial);
		}
		if (loginInicial != null) {
			tfLogin.setText(loginInicial);
			tfLogin.setForeground(tfLogin.originalColor);
		}
		if (senhaInicial != null) {
			pfSenha.setText(senhaInicial);
		}
	}
}
