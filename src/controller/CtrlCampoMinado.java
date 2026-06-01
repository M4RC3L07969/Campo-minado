package controller;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.Partida;
import model.Usuario;
import model.dao.DaoPartida;
import model.dao.DaoUsuario;
import model.jogo.CampoMinado;
import model.jogo.Dificuldade;
import model.jogo.ModoJogo;
import viewer.JanelaJogo;

public class CtrlCampoMinado extends CtrlAbstrato {

    private JanelaJogo janela;
    private CampoMinado jogo;
    private long tempoInicio;
    private int tempoFinal;
    private boolean partidaSalva;
    private Partida partidaCriada;
    private boolean jogoIniciado;
    private boolean loginEmProgresso;

    public CtrlCampoMinado(CtrlAbstrato pai, ModoJogo modo) {
        super(pai);
        this.jogo = new CampoMinado(modo);
        this.tempoInicio = -1;
        this.tempoFinal = -1;
        this.partidaSalva = false;
        this.jogoIniciado = false;
        this.loginEmProgresso = false;
        this.janela = new JanelaJogo(this, this.jogo);
        this.janela.setVisible(true);
    }

    public void abrirCelula(int linha, int coluna) {
        if (!this.jogoIniciado) {
            this.jogoIniciado = true;
            this.tempoInicio = System.currentTimeMillis();
        }
        this.jogo.abrirCelula(linha, coluna);
        this.janela.atualizarTabuleiro();
        this.janela.atualizarStatus();

        if (this.jogo.isJogoEncerrado()) {
            if (this.tempoFinal < 0) {
                this.tempoFinal = (int) ((System.currentTimeMillis() - this.tempoInicio) / 1000);
            }
            this.janela.mostrarResultado(this.jogo.venceu(), this.tempoFinal);
        }
    }

    public void colocarBandeira(int linha, int coluna) {
        if (!this.jogoIniciado) {
            this.jogoIniciado = true;
            this.tempoInicio = System.currentTimeMillis();
        }
        this.jogo.colocarBandeira(linha, coluna);
        this.janela.atualizarTabuleiro();
        this.janela.atualizarStatus();
    }

    public void cliqueDuplo(int linha, int coluna) {
        if (!this.jogoIniciado) {
            return;
        }
        this.jogo.abrirVizinhosSeBandeirasIguais(linha, coluna);
        this.janela.atualizarTabuleiro();
        this.janela.atualizarStatus();

        if (this.jogo.isJogoEncerrado()) {
            if (this.tempoFinal < 0) {
                this.tempoFinal = (int) ((System.currentTimeMillis() - this.tempoInicio) / 1000);
            }
            this.janela.mostrarResultado(this.jogo.venceu(), this.tempoFinal);
        }
    }

    public int getTempoDecorrido() {
        if (!this.jogoIniciado) {
            return 0;
        }
        if (this.jogo.isJogoEncerrado() && this.tempoFinal >= 0) {
            return this.tempoFinal;
        }
        return (int) ((System.currentTimeMillis() - this.tempoInicio) / 1000);
    }

    private Usuario getUsuarioLogado() {
        CtrlAbstrato ctrlPai = this.getCtrlPai();
        if (ctrlPai instanceof CtrlPrograma ctrl) {
            return ctrl.getUsuarioLogado();
        }
        return null;
    }

    public void salvarPartida() {
        if (this.partidaSalva)
            return;

        if (this.loginEmProgresso)
            return;

        if (!this.jogoIniciado || this.tempoFinal < 0)
            return;

        Usuario usuario = getUsuarioLogado();
        if (usuario == null)
            return;

        salvarPartidaEmSegundoPlano(usuario, this.jogo.venceu(), this.jogo.getModoJogo().getDescricao(),
                this.tempoFinal);
    }

    private void salvarPartidaEmSegundoPlano(Usuario usuario, boolean venceu, String modoDescricao, int tempo) {
        if (this.partidaSalva || usuario == null)
            return;

        this.partidaSalva = true;

        Thread threadSalvar = new Thread(() -> salvarPartidaRegistrada(usuario, venceu, modoDescricao, tempo),
                "Salvar partida");
        threadSalvar.setDaemon(true);
        threadSalvar.start();
    }

    private void salvarPartidaRegistrada(Usuario usuario, boolean venceu, String modoDescricao, int tempo) {
        String resultado = venceu ? "Vitoria" : "Derrota";
        Partida partida = new Partida(usuario, modoDescricao, tempo, resultado,
                java.time.LocalDate.now().toString());

        DaoPartida daoPartida = new DaoPartida();
        boolean incluido = daoPartida.incluir(partida);
        if (!incluido) {
            SwingUtilities.invokeLater(() -> this.janela.notificar("Erro ao salvar partida!"));
            return;
        }

        this.partidaCriada = partida;
        usuario.incrementarPartida(venceu);

        if (venceu) {
            Dificuldade dificuldade = Dificuldade.fromModoDescricao(modoDescricao);
            usuario.atualizarMelhorTempo(dificuldade, tempo);
        }

        DaoUsuario daoUsuario = new DaoUsuario();
        boolean atualizado = daoUsuario.alterar(usuario);
        if (!atualizado) {
            SwingUtilities.invokeLater(() -> this.janela.notificar("Erro ao atualizar estatisticas do usuario!"));
        }

        CtrlAbstrato ctrlPai = this.getCtrlPai();
        if (ctrlPai instanceof CtrlPrograma ctrl) {
            SwingUtilities.invokeLater(ctrl::atualizarRanking);
        }
    }

    public void encerrar() {
        this.janela.setVisible(false);
        CtrlAbstrato ctrlPai = this.getCtrlPai();
        if (ctrlPai instanceof CtrlPrograma ctrl) {
            ctrl.setCtrlCampoMinado(null);
        }
    }

    public void tentarSalvarPartidaAposLogin() {
        this.loginEmProgresso = false;
        salvarPartida();
        reiniciarPartida();
    }

    public void reiniciarPartida() {
        this.jogo.reset();
        this.tempoInicio = -1;
        this.tempoFinal = -1;
        this.partidaSalva = false;
        this.partidaCriada = null;
        this.jogoIniciado = false;
        this.loginEmProgresso = false;
        this.janela.atualizarTabuleiro();
        this.janela.atualizarStatus();
        this.janela.atualizarSmiley("😊");
        this.janela.iniciarTimer();
    }

    public void mostrarDialogoResultado(boolean venceu, int tempo) {
        Usuario usuario = getUsuarioLogado();
        CtrlAbstrato ctrlPai = this.getCtrlPai();

        String titulo = venceu ? "🎉 Vitória!" : "💥 Derrota!";
        String mensagem = venceu
                ? "Você venceu em " + tempo + " segundos!"
                : "Você atingiu uma bomba.";

        if (usuario != null) {
            String modoDescricao = this.jogo.getModoJogo().getDescricao();
            int melhorTempoAnterior = 0;
            switch (modoDescricao) {
                case "9x9":
                    melhorTempoAnterior = usuario.getMelhorTempoFacil();
                    break;
                case "16x16":
                    melhorTempoAnterior = usuario.getMelhorTempoMedio();
                    break;
                case "30x16":
                    melhorTempoAnterior = usuario.getMelhorTempoDificil();
                    break;
            }

            if (venceu) {
                int melhorTempoAtual = 0;
                switch (modoDescricao) {
                    case "9x9":
                        melhorTempoAtual = usuario.getMelhorTempoFacil();
                        break;
                    case "16x16":
                        melhorTempoAtual = usuario.getMelhorTempoMedio();
                        break;
                    case "30x16":
                        melhorTempoAtual = usuario.getMelhorTempoDificil();
                        break;
                }

                boolean bateuRecordePessoal = melhorTempoAnterior == 0 || tempo < melhorTempoAnterior;

                if (bateuRecordePessoal) {
                    mensagem += "\n\n🏆 Novo recorde pessoal!";
                } else if (tempo == melhorTempoAnterior) {
                    mensagem += "\n\nIgualou o seu recorde anterior!! Parabéns";
                } else {
                    mensagem += "\n\nSeu melhor tempo: " + melhorTempoAtual + "s";
                }
            }

            Object[] options = { "Nova partida", "Menu principal" };
            int choice = JOptionPane.showOptionDialog(
                    this.janela,
                    mensagem,
                    titulo,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            salvarPartidaEmSegundoPlano(usuario, venceu, modoDescricao, tempo);

            if (choice == 0) {
                reiniciarPartida();
            } else {
                encerrar();
            }
        } else {
            Object[] options = { "Entrar para salvar tempo", "Nova partida", "Menu principal" };
            int choice = JOptionPane.showOptionDialog(
                    this.janela,
                    mensagem,
                    titulo,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                this.loginEmProgresso = true;
                CtrlAbstrato finalCtrlPai = ctrlPai;
                Timer timer = new Timer(100, e -> {
                    if (finalCtrlPai instanceof CtrlPrograma ctrl) {
                        ctrl.iniciarLogin();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else if (choice == 1) {
                reiniciarPartida();
            } else {
                encerrar();
            }
        }
    }

    @Override
    public Object getBemTangivel() {
        return this.partidaCriada;
    }
}
