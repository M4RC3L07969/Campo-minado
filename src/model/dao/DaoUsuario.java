package model.dao;

import model.Usuario;

public class DaoUsuario {

    final public static int TAM_INICIAL_ELEMENTOS = 5;
    final public static int FATOR_CRESCIMENTO = 3;

    private static int numElementos = 0;
    private static Usuario[] arrayDeElementos = new Usuario[TAM_INICIAL_ELEMENTOS];

    public DaoUsuario() {
        super();
    }

    public boolean incluir(Usuario novo) {
        if (novo == null)
            return false;
        if (obterUsuarioPeloNome(novo.getNome()) != null)
            return false;
        if (obterUsuarioPeloLogin(novo.getLogin()) != null)
            return false;
        int tamanho = DaoUsuario.arrayDeElementos.length;
        if (DaoUsuario.numElementos == tamanho) {
            Usuario[] novoArray = new Usuario[tamanho + FATOR_CRESCIMENTO];
            for (int i = 0; i < tamanho; i++)
                novoArray[i] = DaoUsuario.arrayDeElementos[i];
            DaoUsuario.arrayDeElementos = novoArray;
        }
        DaoUsuario.arrayDeElementos[DaoUsuario.numElementos] = novo;
        DaoUsuario.numElementos++;
        return true;
    }

    public boolean remover(Usuario ex) {
        int pos;
        for (pos = 0; pos < DaoUsuario.numElementos; pos++)
            if (DaoUsuario.arrayDeElementos[pos] == ex)
                break;
        if (pos == DaoUsuario.numElementos)
            return false;
        for (int i = pos; i < DaoUsuario.numElementos - 1; i++)
            DaoUsuario.arrayDeElementos[i] = DaoUsuario.arrayDeElementos[i + 1];
        DaoUsuario.arrayDeElementos[DaoUsuario.numElementos - 1] = null;
        DaoUsuario.numElementos--;
        return true;
    }

    public boolean alterar(Usuario alterado) {
        if (alterado == null)
            return false;
        for (int i = 0; i < DaoUsuario.numElementos; i++) {
            if (DaoUsuario.arrayDeElementos[i] == alterado)
                return true;
        }
        return false;
    }

    public Usuario obterUsuarioPeloNome(String nome) {
        for (int i = 0; i < DaoUsuario.numElementos; i++) {
            String nomeDoUsuario = DaoUsuario.arrayDeElementos[i].getNome();
            if (nomeDoUsuario.equals(nome))
                return DaoUsuario.arrayDeElementos[i];
        }
        return null;
    }

    public Usuario obterUsuarioPeloLogin(String login) {
        for (int i = 0; i < DaoUsuario.numElementos; i++) {
            String loginDoUsuario = DaoUsuario.arrayDeElementos[i].getLogin();
            if (loginDoUsuario.equals(login))
                return DaoUsuario.arrayDeElementos[i];
        }
        return null;
    }

    public static Usuario[] obterTodos() {
        Usuario[] resultado = new Usuario[numElementos];
        for (int i = 0; i < numElementos; i++)
            resultado[i] = arrayDeElementos[i];
        return resultado;
    }

    static void recuperarTodos(Usuario[] array) {
        DaoUsuario.arrayDeElementos = array;
        for (numElementos = 0; numElementos < array.length; numElementos++)
            if (array[numElementos] == null)
                break;
    }
}
