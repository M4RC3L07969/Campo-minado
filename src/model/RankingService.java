package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.dao.DaoUsuario;
import model.jogo.Dificuldade;

public class RankingService {

    private static final int TOP_N = 10;

    public List<Usuario> obterRanking(Dificuldade dificuldade) {
        Usuario[] usuarios = DaoUsuario.obterTodos();
        List<Usuario> lista = new ArrayList<>();
        for (Usuario u : usuarios) {
            int tempo = obterTempo(u, dificuldade);
            if (tempo > 0) {
                lista.add(u);
            }
        }
        lista.sort(Comparator
                .comparingInt((Usuario u) -> obterTempo(u, dificuldade))
                .thenComparing(Usuario::getNome));
        if (lista.size() > TOP_N) {
            return new ArrayList<>(lista.subList(0, TOP_N));
        }
        return lista;
    }

    public int obterTempo(Usuario u, Dificuldade dificuldade) {
        switch (dificuldade) {
            case FACIL:
                return u.getMelhorTempoFacil();
            case MEDIO:
                return u.getMelhorTempoMedio();
            case DIFICIL:
                return u.getMelhorTempoDificil();
            default:
                return 0;
        }
    }

    public static String formatarTempo(int segundos) {
        if (segundos <= 0)
            return "—";
        int min = segundos / 60;
        int seg = segundos % 60;
        if (min > 0) {
            return min + "m " + seg + "s";
        }
        return seg + "s";
    }

    public String obterMedalha(int posicao) {
        switch (posicao) {
            case 1:
                return "🥇";
            case 2:
                return "🥈";
            case 3:
                return "🥉";
            default:
                return String.valueOf(posicao);
        }
    }
}
