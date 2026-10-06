package br.com.fiap.campusconsciente.servicos;

import br.com.fiap.campusconsciente.enums.ModoOperacional;
import br.com.fiap.campusconsciente.modelos.HorarioAula;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A rotina da sala: blocos de aula e feriados. A partir dela, decide em
 * qual modo operacional o campus esta num dado momento.
 */
public class GradeHorarios {
    private String sala;
    private HorarioAula[] aulas;
    private LocalDate[] feriados;

    public GradeHorarios() {
        this("Lab. 302", gradePadrao(), new LocalDate[0]);
    }

    public GradeHorarios(String sala, HorarioAula[] aulas, LocalDate[] feriados) {
        this.sala = sala;
        this.aulas = aulas;
        this.feriados = feriados;
    }

    public ModoOperacional modoEm(LocalDateTime momento) {
        int hora = momento.getHour();
        if (hora < 6) return ModoOperacional.MADRUGADA;
        if (isFeriado(momento.toLocalDate())) return ModoOperacional.FERIADO;
        DayOfWeek dia = momento.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) return ModoOperacional.FIM_DE_SEMANA;
        if (temAula(momento)) return ModoOperacional.AULAS;
        if (hora >= 23) return ModoOperacional.ESVAZIAMENTO;
        return ModoOperacional.FORA_DE_AULA;
    }

    public boolean temAula(LocalDateTime momento) {
        for (HorarioAula aula : aulas) {
            if (aula.contem(momento)) return true;
        }
        return false;
    }

    public boolean isFeriado(LocalDate data) {
        for (LocalDate feriado : feriados) {
            if (feriado.equals(data)) return true;
        }
        return false;
    }

    /** Grade do Lab. 302: manha, tarde e noite; a quinta nao tem tarde. */
    private static HorarioAula[] gradePadrao() {
        DayOfWeek[] dias = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        HorarioAula[] grade = new HorarioAula[14];
        int i = 0;
        for (DayOfWeek dia : dias) {
            grade[i++] = new HorarioAula(dia, LocalTime.of(8, 0), LocalTime.of(12, 0));
            if (dia != DayOfWeek.THURSDAY) {
                grade[i++] = new HorarioAula(dia, LocalTime.of(13, 30), LocalTime.of(17, 30));
            }
            grade[i++] = new HorarioAula(dia, LocalTime.of(19, 20), LocalTime.of(22, 55));
        }
        return grade;
    }

    public String getSala() { return sala; }
    public HorarioAula[] getAulas() { return aulas; }
    public LocalDate[] getFeriados() { return feriados; }
}
