package model.jogo;

public enum Dificuldade {
    FACIL("facil", "Fácil", "9x9"),
    MEDIO("medio", "Médio", "16x16"),
    DIFICIL("dificil", "Difícil", "30x16");

    private final String chave;
    private final String label;
    private final String descricaoModo;

    Dificuldade(String chave, String label, String descricaoModo) {
        this.chave = chave;
        this.label = label;
        this.descricaoModo = descricaoModo;
    }

    public String getChave() {
        return chave;
    }

    public String getLabel() {
        return label;
    }

    public String getDescricaoModo() {
        return descricaoModo;
    }

    public static Dificuldade fromModoDescricao(String descricao) {
        for (Dificuldade d : values()) {
            if (d.descricaoModo.equals(descricao)) {
                return d;
            }
        }
        return FACIL;
    }
}
