package controller;

import javax.swing.JOptionPane;

import model.Partida;
import model.Usuario;
import model.dao.DaoPartida;
import model.dao.DaoUsuario;
import model.jogo.CampoMinado;
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

    public CtrlCampoMinado(CtrlAbstrato pai, ModoJogo modo) {
        super(pai);
        this.jogo = new CampoMinado(modo);
        this.tempoInicio = -1;
        this.tempoFinal = -1;
        this.partidaSalva = false;
        this.jogoIniciado = false;
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
            this.salvarPartida();
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
            this.salvarPartida();
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

    public void salvarPartida() {
        if (this.partidaSalva)
            return;

        Usuario usuario = null;
        CtrlAbstrato ctrlPai = this.getCtrlPai();
        if (ctrlPai instanceof CtrlPrograma ctrl) {
            usuario = ctrl.getUsuarioLogado();
        }

        if (usuario == null) {
            Object[] options = { "Login", "Não salvar" };
            int choice = JOptionPane.showOptionDialog(
                    this.janela,
                    "Você deseja salvar a partida?",
                    "Salvar Partida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                if (ctrlPai instanceof CtrlPrograma ctrl) {
                    ctrl.iniciarLogin();
                }
            } else {
                this.partidaSalva = true;
            }
            return;
        }

        this.partidaSalva = true;

        boolean venceu = this.jogo.venceu();
        String resultado = venceu ? "Vitoria" : "Derrota";
        String modoDescricao = this.jogo.getModoJogo().getDescricao();

        this.partidaCriada = new Partida(usuario, modoDescricao, this.tempoFinal, resultado,
                java.time.LocalDate.now().toString());

        DaoPartida daoPartida = new DaoPartida();
        boolean incluido = daoPartida.incluir(this.partidaCriada);
        if (!incluido) {
            this.janela.notificar("Erro ao salvar partida!");
            return;
        }

        usuario.incrementarPartida(venceu);

        if (venceu) {
            String dificuldade;
            switch (modoDescricao) {
                case "9x9":
                    dificuldade = "facil";
                    break;
                case "16x16":
                    dificuldade = "medio";
                    break;
                case "30x16":
                    dificuldade = "dificil";
                    break;
                default:
                    dificuldade = "facil";
            }
            usuario.atualizarMelhorTempo(dificuldade, this.tempoFinal);
        }

        DaoUsuario daoUsuario = new DaoUsuario();
        boolean atualizado = daoUsuario.alterar(usuario);
        if (!atualizado) {
            this.janela.notificar("Erro ao atualizar estatísticas do usuário!");
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
        salvarPartida();
    }

    @Override
    public Object getBemTangivel() {
        return this.partidaCriada;
    }
}
