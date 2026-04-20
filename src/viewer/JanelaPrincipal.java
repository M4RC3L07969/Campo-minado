package viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.CtrlPrograma;

public class JanelaPrincipal extends JanelaAbstrata {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public JanelaPrincipal(CtrlPrograma ctrl) {
		super(ctrl);
		setTitle("Super Trunfo - Menu Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btIncluirCard = new JButton("Incluir Carta");
		btIncluirCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarIncluirCard();
			}
		});
		btIncluirCard.setBounds(30, 40, 150, 30);
		contentPane.add(btIncluirCard);

		JButton btConsultarCards = new JButton("Consultar Cartas");
		btConsultarCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CtrlPrograma ctrl = (CtrlPrograma) getCtrl();
				ctrl.iniciarConsultarCards();
			}
		});
		btConsultarCards.setBounds(200, 40, 150, 30);
		contentPane.add(btConsultarCards);

		JButton btSair = new JButton("Sair");
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCtrl().encerrar();
			}
		});
		btSair.setBounds(130, 100, 120, 30);
		contentPane.add(btSair);
	}
}
