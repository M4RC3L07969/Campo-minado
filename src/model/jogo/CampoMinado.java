package model.jogo;

import java.util.Random;

public class CampoMinado {

    private Celula[][] tabuleiro;
    private int linhas;
    private int colunas;
    private int quantidadeBombas;
    private ModoJogo modoJogo;
    private boolean perdeu;
    private boolean jogoEncerrado;
    private int minasRestantes;
    private int celulasSegurasRestantes;
    private boolean primeiroClique;
    private Random random;
    private int bombaClicadaLinha;
    private int bombaClicadaColuna;
    private boolean bombaClicadaDefinida;

    public CampoMinado(ModoJogo modo) {
        this.modoJogo = modo;
        this.linhas = modo.getLinhas();
        this.colunas = modo.getColunas();
        this.quantidadeBombas = modo.getMinas();
        this.perdeu = false;
        this.jogoEncerrado = false;
        this.minasRestantes = this.quantidadeBombas;
        this.celulasSegurasRestantes = this.linhas * this.colunas - this.quantidadeBombas;
        this.primeiroClique = true;
        this.random = new Random();
        this.bombaClicadaDefinida = false;

        this.tabuleiro = new Celula[this.linhas][this.colunas];
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.tabuleiro[i][j] = new Celula();
            }
        }

        this.gerarBombas();
        this.calcularVizinhos();
    }

    private void gerarBombas() {
        int minasColocadas = 0;
        while (minasColocadas < this.quantidadeBombas) {
            int linha = this.random.nextInt(this.linhas);
            int coluna = this.random.nextInt(this.colunas);
            if (!this.tabuleiro[linha][coluna].isTemBomba()) {
                this.tabuleiro[linha][coluna].setTemBomba(true);
                minasColocadas++;
            }
        }
    }

    private void calcularVizinhos() {
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                int count = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0)
                            continue;
                        int ni = i + di;
                        int nj = j + dj;
                        if (posicaoValida(ni, nj) && this.tabuleiro[ni][nj].isTemBomba()) {
                            count++;
                        }
                    }
                }
                this.tabuleiro[i][j].setMinasAoRedor(count);
            }
        }
    }

    private void recalcularVizinhos() {
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.tabuleiro[i][j].setMinasAoRedor(0);
            }
        }
        this.calcularVizinhos();
    }

    private void reposicionarBomba(int linhaOriginal, int colunaOriginal) {
        this.tabuleiro[linhaOriginal][colunaOriginal].setTemBomba(false);

        while (true) {
            int novaLinha = this.random.nextInt(this.linhas);
            int novaColuna = this.random.nextInt(this.colunas);
            if (novaLinha != linhaOriginal || novaColuna != colunaOriginal) {
                if (!this.tabuleiro[novaLinha][novaColuna].isTemBomba()) {
                    this.tabuleiro[novaLinha][novaColuna].setTemBomba(true);
                    break;
                }
            }
        }
        this.recalcularVizinhos();
    }

    private boolean existeBombaNaAreaInicial(int linha, int coluna) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int ni = linha + di;
                int nj = coluna + dj;
                if (posicaoValida(ni, nj) && this.tabuleiro[ni][nj].isTemBomba()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void reposicionarBombasAreaInicial(int linha, int coluna) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int ni = linha + di;
                int nj = coluna + dj;
                if (posicaoValida(ni, nj) && this.tabuleiro[ni][nj].isTemBomba()) {
                    this.tabuleiro[ni][nj].setTemBomba(false);
                    while (true) {
                        int novaLinha = this.random.nextInt(this.linhas);
                        int novaColuna = this.random.nextInt(this.colunas);
                        if (Math.abs(novaLinha - linha) > 1 || Math.abs(novaColuna - coluna) > 1) {
                            if (!this.tabuleiro[novaLinha][novaColuna].isTemBomba()) {
                                this.tabuleiro[novaLinha][novaColuna].setTemBomba(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
        this.recalcularVizinhos();
    }

    public void abrirCelula(int linha, int coluna) {
        if (this.jogoEncerrado)
            return;

        Celula celula = this.tabuleiro[linha][coluna];

        if (celula.isAberta())
            return;
        if (celula.isBandeira())
            return;

        if (this.primeiroClique) {
            this.primeiroClique = false;
            if (existeBombaNaAreaInicial(linha, coluna)) {
                reposicionarBombasAreaInicial(linha, coluna);
            }
        }

        celula.setAberta(true);

        if (celula.isTemBomba()) {
            this.perdeu = true;
            this.jogoEncerrado = true;
            this.bombaClicadaLinha = linha;
            this.bombaClicadaColuna = coluna;
            this.bombaClicadaDefinida = true;
            this.revelarBombas();
            return;
        }

        this.celulasSegurasRestantes--;

        if (celula.getMinasAoRedor() == 0) {
            this.abrirCelulaRecursivo(linha, coluna);
        }

        this.verificarVitoria();
    }

    private void abrirCelulaRecursivo(int linha, int coluna) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0)
                    continue;
                int ni = linha + di;
                int nj = coluna + dj;

                if (!posicaoValida(ni, nj))
                    continue;
                if (this.jogoEncerrado)
                    return;

                Celula celula = this.tabuleiro[ni][nj];

                if (celula.isAberta())
                    continue;
                if (celula.isBandeira())
                    continue;
                if (celula.isTemBomba())
                    continue;

                celula.setAberta(true);
                this.celulasSegurasRestantes--;

                if (celula.getMinasAoRedor() == 0) {
                    this.abrirCelulaRecursivo(ni, nj);
                }
            }
        }
    }

    public void colocarBandeira(int linha, int coluna) {
        if (this.jogoEncerrado)
            return;

        Celula celula = this.tabuleiro[linha][coluna];
        if (celula.isAberta())
            return;

        Celula.EstadoMarcacao estadoAtual = celula.getEstadoMarcacao();

        switch (estadoAtual) {
            case VAZIO:
                if (this.minasRestantes > 0) {
                    celula.setEstadoMarcacao(Celula.EstadoMarcacao.BANDEIRA);
                    this.minasRestantes--;
                }
                break;
            case BANDEIRA:
                celula.setEstadoMarcacao(Celula.EstadoMarcacao.INTERROGACAO);
                this.minasRestantes++;
                break;
            case INTERROGACAO:
                celula.setEstadoMarcacao(Celula.EstadoMarcacao.VAZIO);
                break;
        }
    }

    private void verificarVitoria() {
        if (this.celulasSegurasRestantes == 0) {
            this.jogoEncerrado = true;
        }
    }

    public boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
    }

    public boolean venceu() {
        return this.jogoEncerrado && !this.perdeu;
    }

    public boolean perdeu() {
        return this.perdeu;
    }

    public boolean isJogoEncerrado() {
        return this.jogoEncerrado;
    }

    public Celula getCelula(int linha, int coluna) {
        return this.tabuleiro[linha][coluna];
    }

    public int getLinhas() {
        return this.linhas;
    }

    public int getColunas() {
        return this.colunas;
    }

    public int getMinasRestantes() {
        return this.minasRestantes;
    }

    public ModoJogo getModoJogo() {
        return this.modoJogo;
    }

    public boolean podeInteragir(int linha, int coluna) {
        return posicaoValida(linha, coluna) && !this.jogoEncerrado && !this.tabuleiro[linha][coluna].isAberta();
    }

    public boolean podeColocarBandeira(int linha, int coluna) {
        return posicaoValida(linha, coluna) && !this.jogoEncerrado && !this.tabuleiro[linha][coluna].isAberta();
    }

    private void revelarBombas() {
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                if (this.tabuleiro[i][j].isTemBomba()) {
                    this.tabuleiro[i][j].setAberta(true);
                }
            }
        }
    }

    public int contarBombasVizinhasManual(int linha, int coluna) {
        int count = 0;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0)
                    continue;
                int ni = linha + di;
                int nj = coluna + dj;
                if (posicaoValida(ni, nj) && this.tabuleiro[ni][nj].isTemBomba()) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isBandeiraClicada(int linha, int coluna) {
        if (!this.bombaClicadaDefinida)
            return false;
        return linha == this.bombaClicadaLinha && coluna == this.bombaClicadaColuna;
    }

    public boolean isBandeiraErrada(int linha, int coluna) {
        Celula celula = this.tabuleiro[linha][coluna];
        return celula.isBandeira() && !celula.isTemBomba();
    }

    public boolean isBandeiraCorreta(int linha, int coluna) {
        Celula celula = this.tabuleiro[linha][coluna];
        return celula.isBandeira() && celula.isTemBomba();
    }

    private int contarBandeirasAoRedor(int linha, int coluna) {
        int count = 0;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0)
                    continue;
                int ni = linha + di;
                int nj = coluna + dj;
                if (posicaoValida(ni, nj) && this.tabuleiro[ni][nj].isBandeira()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void abrirVizinhosSeBandeirasIguais(int linha, int coluna) {
        if (this.jogoEncerrado)
            return;

        Celula celula = this.tabuleiro[linha][coluna];
        if (!celula.isAberta())
            return;

        int minasAoRedor = celula.getMinasAoRedor();
        if (minasAoRedor == 0)
            return;

        int bandeirasAoRedor = contarBandeirasAoRedor(linha, coluna);
        if (bandeirasAoRedor != minasAoRedor)
            return;

        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0)
                    continue;
                int ni = linha + di;
                int nj = coluna + dj;

                if (!posicaoValida(ni, nj))
                    continue;

                Celula vizinha = this.tabuleiro[ni][nj];
                if (!vizinha.isAberta() && !vizinha.isBandeira() && !vizinha.isInterrogacao()) {
                    this.abrirCelula(ni, nj);
                }
            }
        }
    }

    public void reset() {
        this.perdeu = false;
        this.jogoEncerrado = false;
        this.minasRestantes = this.quantidadeBombas;
        this.celulasSegurasRestantes = this.linhas * this.colunas - this.quantidadeBombas;
        this.primeiroClique = true;
        this.bombaClicadaDefinida = false;

        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.tabuleiro[i][j] = new Celula();
            }
        }

        this.gerarBombas();
        this.calcularVizinhos();
    }
}
