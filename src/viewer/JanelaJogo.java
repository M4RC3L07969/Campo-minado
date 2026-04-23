package viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import controller.CtrlCampoMinado;
import model.jogo.CampoMinado;
import model.jogo.Celula;

public class JanelaJogo extends JanelaAbstrata {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton[][] botoes;
    private CampoMinado jogo;
    private JLabel lblMinas;
    private JLabel lblTimer;
    private Timer timerUI;

    private static final Color COR_FECHADA = new Color(189, 189, 189);
    private static final Color COR_ABERTA = new Color(224, 224, 224);
    private static final Color COR_BANDEIRA = new Color(255, 193, 7);
    private static final Color COR_BOMBA = new Color(244, 67, 54);

    private static final Color[] CORES_NUMEROS = {
            null,
            new Color(25, 118, 210),
            new Color(56, 142, 60),
            new Color(211, 47, 47),
            new Color(123, 31, 162),
            new Color(255, 143, 0),
            new Color(0, 151, 167),
            new Color(66, 66, 66),
            new Color(158, 158, 158)
    };

    public JanelaJogo(CtrlCampoMinado ctrl, CampoMinado jogo) {
        super(ctrl);
        this.jogo = jogo;

        int linhas = jogo.getLinhas();
        int colunas = jogo.getColunas();

        setTitle("Campo Minado - " + jogo.getModoJogo().getDescricao());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int largura = Math.min(Math.max(colunas * 38 + 20, 400), 800);
        int altura = Math.min(linhas * 38 + 80, 600);
        setBounds(100, 100, largura, altura);
        setResizable(true);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel painelTabuleiro = new JPanel();
        painelTabuleiro.setLayout(null);
        int tabuleiroLargura = colunas * 36 + 20;
        int tabuleiroAltura = linhas * 36 + 40;
        painelTabuleiro.setPreferredSize(new java.awt.Dimension(tabuleiroLargura, tabuleiroAltura));

        lblMinas = new JLabel("Minas: " + jogo.getMinasRestantes());
        lblMinas.setFont(new Font("Calibri", Font.BOLD, 16));
        lblMinas.setBounds(10, 5, 120, 25);
        contentPane.add(lblMinas);

        lblTimer = new JLabel("Tempo: 0s");
        lblTimer.setFont(new Font("Calibri", Font.BOLD, 16));
        lblTimer.setBounds(largura - 130, 5, 120, 25);
        contentPane.add(lblTimer);

        JScrollPane scroll = new JScrollPane(painelTabuleiro);
        scroll.setBounds(5, 35, largura - 15, altura - 45);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        contentPane.add(scroll);

        this.botoes = new JButton[linhas][colunas];
        int offsetX = 10;
        int offsetY = 10;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                JButton botao = new JButton();
                botao.setBounds(offsetX + j * 36, offsetY + i * 36, 34, 34);
                botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                botao.setBackground(COR_FECHADA);
                botao.setFocusPainted(false);
                botao.setOpaque(true);
                botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                final int linha = i;
                final int coluna = j;

                botao.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
                        if (jogo.getCelula(linha, coluna).isAberta())
                            return;
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (e.isControlDown()) {
                                ctrl.colocarBandeira(linha, coluna);
                            } else {
                                ctrl.abrirCelula(linha, coluna);
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            ctrl.colocarBandeira(linha, coluna);
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
                            ctrl.cliqueDuplo(linha, coluna);
                        }
                    }
                });

                this.botoes[i][j] = botao;
                painelTabuleiro.add(botao);
            }
        }

        this.timerUI = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarStatus();
            }
        });
        this.timerUI.start();
    }

    public void atualizarTabuleiro() {
        int linhas = this.jogo.getLinhas();
        int colunas = this.jogo.getColunas();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = this.jogo.getCelula(i, j);
                JButton botao = this.botoes[i][j];

                if (celula.isAberta()) {
                    botao.setBackground(COR_ABERTA);
                    botao.setEnabled(true);
                    botao.setFocusable(false);
                    botao.setBorder(null);

                    if (celula.isTemBomba()) {
                        botao.setBackground(COR_BOMBA);
                        if (this.jogo.isBandeiraClicada(i, j)) {
                            botao.setText("💥");
                        } else {
                            botao.setText("💣");
                        }
                        botao.setForeground(Color.BLACK);
                        botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    } else if (celula.getMinasAoRedor() > 0) {
                        int n = celula.getMinasAoRedor();
                        botao.setText(String.valueOf(n));
                        botao.setFont(new Font("Arial", Font.BOLD, 14));
                        if (n < CORES_NUMEROS.length && CORES_NUMEROS[n] != null) {
                            botao.setForeground(CORES_NUMEROS[n]);
                        } else {
                            botao.setForeground(Color.BLACK);
                        }
                    } else {
                        botao.setText("");
                        botao.setForeground(Color.BLACK);
                        botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    }
                } else if (celula.isBandeira()) {
                    botao.setBackground(COR_BANDEIRA);
                    if (this.jogo.isJogoEncerrado() && this.jogo.isBandeiraErrada(i, j)) {
                        botao.setText("❌");
                    } else {
                        botao.setText("🚩");
                    }
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else if (celula.isInterrogacao()) {
                    botao.setBackground(COR_FECHADA);
                    botao.setText("❓");
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else {
                    botao.setBackground(COR_FECHADA);
                    botao.setText("");
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setEnabled(true);
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
            }
        }
    }

    public void atualizarStatus() {
        this.lblMinas.setText("Minas: " + this.jogo.getMinasRestantes());
        CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
        int tempo = ctrl.getTempoDecorrido();
        this.lblTimer.setText("Tempo: " + tempo + "s");
    }

    public void mostrarResultado(boolean venceu, int tempo) {
        this.timerUI.stop();

        if (venceu) {
            notificar("Vitória! Você venceu em " + tempo + " segundos!");
        } else {
            notificar("Derrota! Você atingiu uma bomba.");
        }
    }
}
