package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public enum PeriodoRanking {
    SEMANAL("Semanal"),
    MENSAL("Mensal"),
    TOTAL("Total");

    private final String label;

    PeriodoRanking(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getDataInicio() {
        LocalDate hoje = LocalDate.now();
        switch (this) {
            case SEMANAL:
                return hoje.with(DayOfWeek.MONDAY);
            case MENSAL:
                return hoje.withDayOfMonth(1);
            case TOTAL:
            default:
                return null;
        }
    }

    public LocalDateTime getProximoReinicio() {
        LocalDateTime agora = LocalDateTime.now();
        switch (this) {
            case SEMANAL:
                return agora.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
            case MENSAL:
                LocalDate primeiroDiaProximoMes = agora.toLocalDate().withDayOfMonth(1).plusMonths(1);
                return primeiroDiaProximoMes.atStartOfDay();
            case TOTAL:
            default:
                return null;
        }
    }
}
