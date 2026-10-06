package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.ModoOperacional;

/**
 * Sensor de temperatura colado na carcaca do gabinete. Detecta o
 * superaquecimento antes do fogo. Risco de incendio nao depende do
 * horario, entao o contexto e a repeticao sao ignorados.
 */
public class SensorTemperatura extends Sensor {
    private double ultimaLeitura;

    public SensorTemperatura() {
        super("DHT-00", "gabinete");
    }

    public SensorTemperatura(String id, String localizacao) {
        super(id, localizacao);
    }

    @Override
    public double calcularPontuacao(double temperatura, ModoOperacional modo,
                                    double fatorRepeticao, double fatorCorroboracao) {
        this.ultimaLeitura = temperatura;
        if (temperatura >= 75) return 100;
        if (temperatura >= 65) return 80;
        if (temperatura >= 55) return 50;
        if (temperatura >= 48) return 25;
        return 0;
    }

    @Override
    public boolean detectaPresenca() {
        return false;
    }

    @Override
    public String getTipo() {
        return "Sensor de temperatura";
    }

    public double getUltimaLeitura() { return ultimaLeitura; }
}
