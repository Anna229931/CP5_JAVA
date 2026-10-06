package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.ModoOperacional;

/**
 * Classe mae de todos os sensores do campus. Define o que todo sensor
 * tem em comum e obriga cada tipo a dizer como sua leitura vira pontos.
 */
public abstract class Sensor {
    private String id;
    private String localizacao;
    private boolean ativo;
    private static int totalSensores = 0;

    public Sensor() {
        this("SEM-ID", "nao informada");
    }

    public Sensor(String id, String localizacao) {
        this.id = id;
        this.localizacao = localizacao;
        this.ativo = true;
        totalSensores++;
    }

    /**
     * Polimorfismo: cada tipo de sensor calcula sua pontuacao de risco
     * de um jeito diferente.
     */
    public abstract double calcularPontuacao(double valorLido, ModoOperacional modo,
                                             double fatorRepeticao, double fatorCorroboracao);

    /** Indica se o sensor detecta presenca humana (usado na corroboracao). */
    public abstract boolean detectaPresenca();

    public abstract String getTipo();

    public void desativar() { this.ativo = false; }
    public void ativar() { this.ativo = true; }

    public String getId() { return id; }
    public String getLocalizacao() { return localizacao; }
    public boolean isAtivo() { return ativo; }
    public static int getTotalSensores() { return totalSensores; }

    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    @Override
    public String toString() {
        return getTipo() + " [" + id + "] em " + localizacao + (ativo ? "" : " (inativo)");
    }
}
