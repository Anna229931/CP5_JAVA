package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.NivelSeguranca;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Uma leitura de sensor registrada no sistema, com o risco calculado. */
public class Evento {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("EEE dd/MM HH:mm:ss", Locale.forLanguageTag("pt-BR"));
    private Sensor sensor;
    private double valor;
    private LocalDateTime momento;
    private double pontuacao;
    private NivelSeguranca nivel;
    private String detalhe;

    public Evento() {
        this.momento = LocalDateTime.now();
        this.nivel = NivelSeguranca.VERDE;
        this.detalhe = "";
    }

    public Evento(Sensor sensor, double valor, LocalDateTime momento) {
        this.sensor = sensor;
        this.valor = valor;
        this.momento = momento;
        this.nivel = NivelSeguranca.VERDE;
        this.detalhe = "";
    }

    /** Recebe o resultado do motor de score. */
    public void registrarAvaliacao(double pontuacao, String detalhe) {
        this.pontuacao = Math.round(pontuacao * 10) / 10.0;
        this.nivel = NivelSeguranca.classificar(pontuacao);
        this.detalhe = detalhe;
    }

    public Sensor getSensor() { return sensor; }
    public double getValor() { return valor; }
    public LocalDateTime getMomento() { return momento; }
    public double getPontuacao() { return pontuacao; }
    public NivelSeguranca getNivel() { return nivel; }
    public String getDetalhe() { return detalhe; }

    public String getMomentoFormatado() { return momento.format(FORMATO); }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %.1f pts (%s)", getMomentoFormatado(),
                sensor.getTipo(), pontuacao, nivel);
    }
}
