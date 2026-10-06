package br.com.fiap.campusconsciente.modelos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** Um bloco de aula da grade de ocupacao da sala. */
public class HorarioAula {
    private DayOfWeek dia;
    private LocalTime inicio;
    private LocalTime fim;

    public HorarioAula() {
        this(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));
    }

    public HorarioAula(DayOfWeek dia, LocalTime inicio, LocalTime fim) {
        this.dia = dia;
        this.inicio = inicio;
        this.fim = fim;
    }

    public boolean contem(LocalDateTime momento) {
        LocalTime hora = momento.toLocalTime();
        return momento.getDayOfWeek() == dia && !hora.isBefore(inicio) && !hora.isAfter(fim);
    }

    public DayOfWeek getDia() { return dia; }
    public LocalTime getInicio() { return inicio; }
    public LocalTime getFim() { return fim; }
}
