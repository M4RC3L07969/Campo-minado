package model.dao;

import model.Card;

public class DaoCard {
    //
    // CONSTANTES
    //
    final public static int TAM_INICIAL_ELEMENTOS = 5;
    final public static int FATOR_CRESCIMENTO = 3;
    //
    // ATRIBUTOS
    //
    private static int numElementos = 0;
    private static Card[] arrayDeElementos = new Card[TAM_INICIAL_ELEMENTOS];

    //
    // MÉTODOS
    //
    public DaoCard() {
        super();
    }

    public boolean incluir(Card novo) {
        if (novo == null)
            return false;
        int tamanho = DaoCard.arrayDeElementos.length;
        if (DaoCard.numElementos == tamanho) {
            Card[] novoArray = new Card[tamanho + FATOR_CRESCIMENTO];
            for (int i = 0; i < tamanho; i++)
                novoArray[i] = DaoCard.arrayDeElementos[i];
            DaoCard.arrayDeElementos = novoArray;
        }
        DaoCard.arrayDeElementos[DaoCard.numElementos] = novo;
        DaoCard.numElementos++;
        return true;
    }

    public boolean remover(Card ex) {
        int pos;
        for (pos = 0; pos < DaoCard.numElementos; pos++)
            if (DaoCard.arrayDeElementos[pos] == ex)
                break;
        if (pos == DaoCard.numElementos)
            return false;
        for (int i = pos; i < DaoCard.numElementos - 1; i++)
            DaoCard.arrayDeElementos[i] = DaoCard.arrayDeElementos[i + 1];
        DaoCard.arrayDeElementos[DaoCard.numElementos - 1] = null;
        DaoCard.numElementos--;
        return true;
    }

    public boolean alterar(Card alterado) {
        if (alterado == null)
            return false;
        for (int i = 0; i < DaoCard.numElementos; i++) {
            if (DaoCard.arrayDeElementos[i] == alterado)
                return true;
        }
        return false;
    }

    public Card obterCardPeloNome(String nome) {
        for (int i = 0; i < DaoCard.numElementos; i++) {
            String nomeDoCard = DaoCard.arrayDeElementos[i].getNome();
            if (nomeDoCard.equals(nome))
                return DaoCard.arrayDeElementos[i];
        }
        return null;
    }

    public static Card[] obterTodos() {
        Card[] resultado = new Card[numElementos];
        for (int i = 0; i < numElementos; i++)
            resultado[i] = arrayDeElementos[i];
        return resultado;
    }

    static void recuperarTodos(Card[] array) {
        DaoCard.arrayDeElementos = array;
        for (numElementos = 0; numElementos < array.length; numElementos++)
            if (array[numElementos] == null)
                break;
    }
}
