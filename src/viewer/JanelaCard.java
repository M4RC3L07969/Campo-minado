package viewer;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.CtrlAbstrato;
import controller.CtrlIncluirCard;
import controller.CtrlAlterarCard;
import model.Card;

public class JanelaCard extends JanelaAbstrata {
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField tfVelocidade;
	private JTextField tfForca;
	private JTextField tfInteligencia;
	private JTextField tfHabilidade;
	private JLabel lblPreview;
	private byte[] imagemSelecionada;

	public JanelaCard(CtrlAbstrato ctrl) {
		super(ctrl);
		setTitle("Carta");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 500, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblNome.setBounds(33, 30, 60, 14);
		contentPane.add(lblNome);

		tfNome = new JTextField();
		tfNome.setBounds(100, 25, 350, 20);
		contentPane.add(tfNome);
		tfNome.setColumns(10);

		JLabel lblVelocidade = new JLabel("Velocidade:");
		lblVelocidade.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblVelocidade.setBounds(33, 60, 80, 14);
		contentPane.add(lblVelocidade);

		tfVelocidade = new JTextField();
		tfVelocidade.setBounds(120, 55, 60, 20);
		contentPane.add(tfVelocidade);
		tfVelocidade.setColumns(10);

		JLabel lblForca = new JLabel("Força:");
		lblForca.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblForca.setBounds(200, 60, 50, 14);
		contentPane.add(lblForca);

		tfForca = new JTextField();
		tfForca.setBounds(250, 55, 60, 20);
		contentPane.add(tfForca);
		tfForca.setColumns(10);

		JLabel lblInteligencia = new JLabel("Inteligência:");
		lblInteligencia.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblInteligencia.setBounds(33, 90, 85, 14);
		contentPane.add(lblInteligencia);

		tfInteligencia = new JTextField();
		tfInteligencia.setBounds(120, 85, 60, 20);
		contentPane.add(tfInteligencia);
		tfInteligencia.setColumns(10);

		JLabel lblHabilidade = new JLabel("Habilidade:");
		lblHabilidade.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblHabilidade.setBounds(200, 90, 80, 14);
		contentPane.add(lblHabilidade);

		tfHabilidade = new JTextField();
		tfHabilidade.setBounds(280, 85, 60, 20);
		contentPane.add(tfHabilidade);
		tfHabilidade.setColumns(10);

		JButton btSelecionarImagem = new JButton("Selecionar Imagem");
		btSelecionarImagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"Imagens (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));
				int resultado = fileChooser.showOpenDialog(JanelaCard.this);
				if (resultado == JFileChooser.APPROVE_OPTION) {
					File arquivo = fileChooser.getSelectedFile();
					try {
						imagemSelecionada = Files.readAllBytes(arquivo.toPath());
						exibirPreview(imagemSelecionada);
					} catch (IOException ex) {
						notificar("Erro ao carregar a imagem: " + ex.getMessage());
					}
				}
			}
		});
		btSelecionarImagem.setBounds(33, 120, 150, 23);
		contentPane.add(btSelecionarImagem);

		lblPreview = new JLabel("Sem imagem");
		lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreview.setBounds(33, 155, 430, 130);
		lblPreview.setBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
		contentPane.add(lblPreview);

		JButton btOk = new JButton("Ok");
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = tfNome.getText();
				String strVelocidade = tfVelocidade.getText();
				String strForca = tfForca.getText();
				String strInteligencia = tfInteligencia.getText();
				String strHabilidade = tfHabilidade.getText();

				int velocidade, forca, inteligencia, habilidade;
				try {
					velocidade = Integer.parseInt(strVelocidade);
					forca = Integer.parseInt(strForca);
					inteligencia = Integer.parseInt(strInteligencia);
					habilidade = Integer.parseInt(strHabilidade);
				} catch (NumberFormatException ex) {
					notificar("Os atributos devem ser números inteiros!");
					return;
				}

				if (getCtrl() instanceof CtrlIncluirCard ctrl) {
					ctrl.incluirCard(nome, velocidade, forca, inteligencia, habilidade, imagemSelecionada);
				} else if (getCtrl() instanceof CtrlAlterarCard ctrl) {
					ctrl.alterarCard(nome, velocidade, forca, inteligencia, habilidade, imagemSelecionada);
				} else {
					JOptionPane.showMessageDialog(btOk, "Controlador inválido.");
				}
			}
		});
		btOk.setBounds(120, 300, 89, 23);
		contentPane.add(btOk);

		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btCancelar.setBounds(270, 300, 89, 23);
		contentPane.add(btCancelar);
	}

	public void preencherCampos(Card c) {
		tfNome.setText(c.getNome());
		tfVelocidade.setText(String.valueOf(c.getVelocidade()));
		tfForca.setText(String.valueOf(c.getForca()));
		tfInteligencia.setText(String.valueOf(c.getInteligencia()));
		tfHabilidade.setText(String.valueOf(c.getHabilidade()));
		this.imagemSelecionada = c.getImagem();
		if (c.getImagem() != null && c.getImagem().length > 0) {
			exibirPreview(c.getImagem());
		}
	}

	private void exibirPreview(byte[] bytes) {
		ImageIcon icon = new ImageIcon(bytes);
		Image img = icon.getImage().getScaledInstance(lblPreview.getWidth(), lblPreview.getHeight(), Image.SCALE_SMOOTH);
		lblPreview.setIcon(new ImageIcon(img));
		lblPreview.setText(null);
	}
}
