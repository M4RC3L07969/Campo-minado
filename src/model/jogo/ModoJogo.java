package model.jogo;

public enum ModoJogo {

    FACIL("9x9", 9, 9, 10),
    MEDIO("16x16", 16, 16, 40),
    DIFICIL("30x16", 30, 16, 99);

    private final String descricao;
    private final int linhas;
    private final int colunas;
    private final int minas;

    ModoJogo(String descricao, int linhas, int colunas, int minas) {
        this.descricao = descricao;
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public int getLinhas() {
        return this.linhas;
    }

    public int getColunas() {
        return this.colunas;
    }

    public int getMinas() {
        return this.minas;
    }

    public static ModoJogo fromDescricao(String descricao) {
        for (ModoJogo modo : values()) {
            if (modo.getDescricao().equals(descricao)) {
                return modo;
            }
        }
        throw new IllegalArgumentException("Modo inválido: " + descricao);
    }
}
