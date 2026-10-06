package br.com.fiap.campusconsciente.servicos;

import br.com.fiap.campusconsciente.enums.ModoOperacional;
import br.com.fiap.campusconsciente.modelos.Evento;
import br.com.fiap.campusconsciente.modelos.Sensor;

import java.time.Duration;

/**
 * O cerebro do sistema. Junta o contexto (grade), a memoria de curto
 * prazo (repeticao) e o cruzamento de fontes (corroboracao), e pede ao
 * proprio sensor que calcule sua pontuacao.
 */
public class MotorDeScore {
    private GradeHorarios grade;
    private int janelaMinutos;

    public MotorDeScore() {
        this(new GradeHorarios(), 2);
    }

    public MotorDeScore(GradeHorarios grade, int janelaMinutos) {
        this.grade = grade;
        this.janelaMinutos = janelaMinutos;
    }

    public void avaliar(Evento evento, Evento[] historico, int totalHistorico) {
        Sensor sensor = evento.getSensor();
        ModoOperacional modo = grade.modoEm(evento.getMomento());

        int repeticoes = 1;      // o proprio evento conta
        boolean corroborado = false;
        String confirmadoPor = "";
        for (int i = 0; i < totalHistorico; i++) {
            Evento anterior = historico[i];
            long minutos = Duration.between(anterior.getMomento(), evento.getMomento()).toMinutes();
            if (minutos < 0 || minutos > janelaMinutos) continue;
            if (anterior.getSensor() == sensor) {
                repeticoes++;
            } else if (sensor.detectaPresenca() && anterior.getSensor().detectaPresenca()) {
                corroborado = true;
                confirmadoPor = anterior.getSensor().getTipo();
            }
        }
        double fatorRepeticao = repeticoes <= 1 ? 1.0 : repeticoes <= 3 ? 1.3 : repeticoes <= 5 ? 1.6 : 2.0;
        double fatorCorroboracao = corroborado ? 1.5 : 1.0;

        // Polimorfismo: cada sensor sabe calcular a propria pontuacao.
        double pontuacao = sensor.calcularPontuacao(evento.getValor(), modo, fatorRepeticao, fatorCorroboracao);

        String detalhe;
        if (sensor.detectaPresenca()) {
            detalhe = "modo " + modo + " (fator " + modo.getFator() + ")";
            if (modo == ModoOperacional.AULAS) {
                detalhe += " - presenca esperada, sem escalada";
            } else {
                detalhe += ", repeticao " + fatorRepeticao;
                if (corroborado) detalhe += ", corroboracao 1.5 (confirmado por " + confirmadoPor + ")";
            }
        } else {
            detalhe = "leitura " + evento.getValor() + " - limiar fixo, sem fator de horario";
        }
        evento.registrarAvaliacao(pontuacao, detalhe);
    }

    public GradeHorarios getGrade() { return grade; }
    public int getJanelaMinutos() { return janelaMinutos; }
}
