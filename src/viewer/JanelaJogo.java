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
import util.ThemeManager;

public class JanelaJogo extends JanelaAbstrata {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton[][] botoes;
    private CampoMinado jogo;
    private JLabel lblMinas;
    private JLabel lblTimer;
    private JButton btnSmiley;
    private Timer timerUI;

    private static final Color COR_FECHADA_LIGHT = new Color(189, 189, 189);
    private static final Color COR_ABERTA_LIGHT = new Color(224, 224, 224);
    private static final Color COR_FECHADA_DARK = new Color(60, 60, 60);
    private static final Color COR_ABERTA_DARK = new Color(100, 100, 100);
    private static final Color COR_BANDEIRA = new Color(255, 193, 7);
    private static final Color COR_BANDEIRA_CORRETA = new Color(76, 175, 80);
    private static final Color COR_BOMBA = new Color(244, 67, 54);

    private static final Color[] CORES_NUMEROS_LIGHT = {
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

    private static final Color[] CORES_NUMEROS_DARK = {
            null,
            new Color(100, 181, 246),
            new Color(129, 199, 132),
            new Color(239, 154, 154),
            new Color(186, 104, 200),
            new Color(255, 204, 128),
            new Color(77, 208, 225),
            new Color(200, 200, 200),
            new Color(255, 255, 255)
    };

    private Color getCorFechada() {
        return ThemeManager.getInstance().isDarkMode() ? COR_FECHADA_DARK : COR_FECHADA_LIGHT;
    }

    private Color getCorAberta() {
        return ThemeManager.getInstance().isDarkMode() ? COR_ABERTA_DARK : COR_ABERTA_LIGHT;
    }

    private Color[] getCorNumeros() {
        return ThemeManager.getInstance().isDarkMode() ? CORES_NUMEROS_DARK : CORES_NUMEROS_LIGHT;
    }

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

        btnSmiley = new JButton("😊");
        btnSmiley.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        btnSmiley.setBounds(largura / 2 - 25, 5, 50, 35);
        btnSmiley.setFocusPainted(false);
        btnSmiley.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                btnSmiley.setText("😮");
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
                ctrl.reiniciarPartida();
            }
        });
        contentPane.add(btnSmiley);

        getRootPane().registerKeyboardAction(
                e -> {
                    CtrlCampoMinado ctrlCampo = (CtrlCampoMinado) getCtrl();
                    ctrlCampo.reiniciarPartida();
                },
                javax.swing.KeyStroke.getKeyStroke("F2"),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

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
                botao.setBackground(getCorFechada());
                botao.setFocusPainted(false);
                botao.setOpaque(true);
                botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                final int linha = i;
                final int coluna = j;

                botao.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
                        Celula celula = jogo.getCelula(linha, coluna);
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (e.isControlDown()) {
                                ctrl.colocarBandeira(linha, coluna);
                            } else if (celula.isAberta() && celula.getMinasAoRedor() > 0) {
                                aplicarEfeitoVizinhos(linha, coluna, true);
                            } else if (celula.isAberta()) {
                                ctrl.cliqueDuplo(linha, coluna);
                            } else {
                                botao.getModel().setPressed(true);
                                botao.getModel().setArmed(true);
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            ctrl.colocarBandeira(linha, coluna);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
                        Celula celula = jogo.getCelula(linha, coluna);
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (!e.isControlDown() && celula.isAberta() && celula.getMinasAoRedor() > 0) {
                                aplicarEfeitoVizinhos(linha, coluna, false);
                                ctrl.cliqueDuplo(linha, coluna);
                            } else if (!celula.isAberta()) {
                                botao.getModel().setPressed(false);
                                botao.getModel().setArmed(false);
                                ctrl.abrirCelula(linha, coluna);
                            }
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

    private void aplicarEfeitoVizinhos(int linha, int coluna, boolean pressionar) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0)
                    continue;
                int ni = linha + di;
                int nj = coluna + dj;
                if (ni >= 0 && ni < this.jogo.getLinhas() && nj >= 0 && nj < this.jogo.getColunas()) {
                    Celula vizinha = this.jogo.getCelula(ni, nj);
                    JButton btnVizinho = this.botoes[ni][nj];
                    if (!vizinha.isAberta() && !vizinha.isBandeira()) {
                        btnVizinho.getModel().setPressed(pressionar);
                        btnVizinho.getModel().setArmed(pressionar);
                    }
                }
            }
        }
    }

    public void atualizarTabuleiro() {
        int linhas = this.jogo.getLinhas();
        int colunas = this.jogo.getColunas();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Celula celula = this.jogo.getCelula(i, j);
                JButton botao = this.botoes[i][j];

                botao.getModel().setPressed(false);
                botao.getModel().setArmed(false);

                if (celula.isAberta()) {
                    botao.setBackground(getCorAberta());
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
                        Color[] cores = getCorNumeros();
                        if (n < cores.length && cores[n] != null) {
                            botao.setForeground(cores[n]);
                        } else {
                            botao.setForeground(ThemeManager.getInstance().isDarkMode() ? Color.WHITE : Color.BLACK);
                        }
                    } else {
                        botao.setText("");
                        botao.setForeground(Color.BLACK);
                        botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    }
                } else if (celula.isBandeira()) {
                    if (this.jogo.isJogoEncerrado() && this.jogo.venceu() && this.jogo.isBandeiraCorreta(i, j)) {
                        botao.setBackground(COR_BANDEIRA_CORRETA);
                    } else {
                        botao.setBackground(COR_BANDEIRA);
                    }
                    if (this.jogo.isJogoEncerrado() && this.jogo.isBandeiraErrada(i, j)) {
                        botao.setText("❌");
                    } else {
                        botao.setText("🚩");
                    }
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else if (this.jogo.isJogoEncerrado() && this.jogo.venceu() && celula.isTemBomba()
                        && !celula.isBandeira()) {
                    botao.setBackground(COR_BANDEIRA);
                    botao.setText("🚩");
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else if (celula.isInterrogacao()) {
                    botao.setBackground(getCorFechada());
                    botao.setText("❓");
                    botao.setForeground(Color.BLACK);
                    botao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    botao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else {
                    botao.setBackground(getCorFechada());
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

    public void iniciarTimer() {
        this.timerUI.start();
    }

    public void pararTimer() {
        this.timerUI.stop();
    }

    public void mostrarResultado(boolean venceu, int tempo) {
        this.timerUI.stop();

        if (venceu) {
            atualizarSmiley("😎");
        } else {
            atualizarSmiley("😵");
        }

        CtrlCampoMinado ctrl = (CtrlCampoMinado) getCtrl();
        ctrl.mostrarDialogoResultado(venceu, tempo);
    }

    public void atualizarSmiley(String emoji) {
        this.btnSmiley.setText(emoji);
    }

    public void reiniciarTimer() {
        this.timerUI.start();
    }
}
