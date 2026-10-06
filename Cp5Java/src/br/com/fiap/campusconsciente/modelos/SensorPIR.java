package br.com.fiap.campusconsciente.modelos;

/**
 * Sensor de presenca infravermelho, instalado no ponto cego da camera.
 * Detecta movimento, mas pode ser enganado por um ventilador: por isso
 * vale menos pontos que a camera.
 */
public class SensorPIR extends SensorDePresenca {
    public static final double PONTOS_BASE = 30;
    private int alcanceMetros;

    public SensorPIR() {
        super("PIR-00", "ponto cego", PONTOS_BASE);
        this.alcanceMetros = 7;
    }

    public SensorPIR(String id, String localizacao, int alcanceMetros) {
        super(id, localizacao, PONTOS_BASE);
        this.alcanceMetros = alcanceMetros;
    }

    @Override
    public String getTipo() {
        return "Sensor de presenca (PIR)";
    }

    public int getAlcanceMetros() { return alcanceMetros; }
}
