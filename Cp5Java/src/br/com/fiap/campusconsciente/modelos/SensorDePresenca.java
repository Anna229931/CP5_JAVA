package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.ModoOperacional;

/**
 * Sensores que detectam pessoas. Todos seguem a mesma formula:
 * pontos base x contexto x repeticao x corroboracao, limitada a 100.
 */
public abstract class SensorDePresenca extends Sensor {
    private double pontosBase;

    public SensorDePresenca() {
        super();
        this.pontosBase = 10;
    }

    public SensorDePresenca(String id, String localizacao, double pontosBase) {
        super(id, localizacao);
        this.pontosBase = pontosBase;
    }

    @Override
    public double calcularPontuacao(double valorLido, ModoOperacional modo,
                                    double fatorRepeticao, double fatorCorroboracao) {
        // Durante a aula, presenca continua e o esperado: nao escala.
        if (modo == ModoOperacional.AULAS) {
            fatorRepeticao = 1.0;
            fatorCorroboracao = 1.0;
        }
        double pontuacao = pontosBase * modo.getFator() * fatorRepeticao * fatorCorroboracao;
        return Math.min(pontuacao, 100);
    }

    @Override
    public boolean detectaPresenca() {
        return true;
    }

    public double getPontosBase() { return pontosBase; }
}
