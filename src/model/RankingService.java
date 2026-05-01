package model;

import java.util.Comparator;
import java.util.List;

import model.dao.DaoPartida;
import model.jogo.Dificuldade;

public class RankingService {

    private static final int TOP_N = 10;

    public List<RankingEntry> obterRanking(Dificuldade dificuldade, PeriodoRanking periodo) {
        String modo = dificuldadeToModo(dificuldade);
        List<RankingEntry> lista = DaoPartida.obterMelhoresTemposPorPeriodo(modo, periodo);

        lista.sort(Comparator
                .comparingInt(RankingEntry::getMelhorTempo)
                .thenComparing(e -> e.getUsuario().getNome()));

        if (lista.size() > TOP_N) {
            return lista.subList(0, TOP_N);
        }
        return lista;
    }

    public int obterMelhorTempoUsuario(Usuario usuario, Dificuldade dificuldade, PeriodoRanking periodo) {
        String modo = dificuldadeToModo(dificuldade);
        List<RankingEntry> lista = DaoPartida.obterMelhoresTemposPorPeriodo(modo, periodo);

        for (RankingEntry entry : lista) {
            if (entry.getUsuario().getId() == usuario.getId()) {
                return entry.getMelhorTempo();
            }
        }
        return 0;
    }

    private String dificuldadeToModo(Dificuldade dificuldade) {
        switch (dificuldade) {
            case FACIL:
                return "9x9";
            case MEDIO:
                return "16x16";
            case DIFICIL:
                return "30x16";
            default:
                return "";
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
