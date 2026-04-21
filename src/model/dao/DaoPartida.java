package model.dao;

import model.Partida;

public class DaoPartida {

    final public static int TAM_INICIAL_ELEMENTOS = 5;
    final public static int FATOR_CRESCIMENTO = 3;

    private static int numElementos = 0;
    private static Partida[] arrayDeElementos = new Partida[TAM_INICIAL_ELEMENTOS];

    public DaoPartida() {
        super();
    }

    public boolean incluir(Partida novo) {
        if (novo == null)
            return false;
        int tamanho = DaoPartida.arrayDeElementos.length;
        if (DaoPartida.numElementos == tamanho) {
            Partida[] novoArray = new Partida[tamanho + FATOR_CRESCIMENTO];
            for (int i = 0; i < tamanho; i++)
                novoArray[i] = DaoPartida.arrayDeElementos[i];
            DaoPartida.arrayDeElementos = novoArray;
        }
        DaoPartida.arrayDeElementos[DaoPartida.numElementos] = novo;
        DaoPartida.numElementos++;
        return true;
    }

    public boolean remover(Partida ex) {
        int pos;
        for (pos = 0; pos < DaoPartida.numElementos; pos++)
            if (DaoPartida.arrayDeElementos[pos] == ex)
                break;
        if (pos == DaoPartida.numElementos)
            return false;
        for (int i = pos; i < DaoPartida.numElementos - 1; i++)
            DaoPartida.arrayDeElementos[i] = DaoPartida.arrayDeElementos[i + 1];
        DaoPartida.arrayDeElementos[DaoPartida.numElementos - 1] = null;
        DaoPartida.numElementos--;
        return true;
    }

    public boolean alterar(Partida alterado) {
        if (alterado == null)
            return false;
        for (int i = 0; i < DaoPartida.numElementos; i++) {
            if (DaoPartida.arrayDeElementos[i] == alterado)
                return true;
        }
        return false;
    }

    public void removerPartidasPorUsuario(model.Usuario usuario) {
        for (int i = DaoPartida.numElementos - 1; i >= 0; i--) {
            Partida partida = DaoPartida.arrayDeElementos[i];
            if (partida != null && partida.getUsuario() == usuario) {
                for (int j = i; j < DaoPartida.numElementos - 1; j++)
                    DaoPartida.arrayDeElementos[j] = DaoPartida.arrayDeElementos[j + 1];
                DaoPartida.arrayDeElementos[DaoPartida.numElementos - 1] = null;
                DaoPartida.numElementos--;
            }
        }
    }

    public static Partida[] obterTodos() {
        Partida[] resultado = new Partida[numElementos];
        for (int i = 0; i < numElementos; i++)
            resultado[i] = arrayDeElementos[i];
        return resultado;
    }

    static void recuperarTodos(Partida[] array) {
        DaoPartida.arrayDeElementos = array;
        for (numElementos = 0; numElementos < array.length; numElementos++)
            if (array[numElementos] == null)
                break;
    }
}
