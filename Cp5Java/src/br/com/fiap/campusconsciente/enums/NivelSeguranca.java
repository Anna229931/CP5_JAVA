package br.com.fiap.campusconsciente.enums;

/**
 * Os quatro niveis de seguranca do sistema. Cada nivel tem a pontuacao
 * minima a partir da qual ele vale e a resposta fisica correspondente.
 */
public enum NivelSeguranca {
    VERDE(0, "Tudo calmo", "registra e monitora"),
    AMARELO(25, "Atencao", "sinaliza no ambiente"),
    LARANJA(50, "Alerta", "sinaliza no ambiente e avisa o responsavel"),
    VERMELHO(75, "Critico", "aciona luz, sirene e aviso ao responsavel");

    private final double pontuacaoMinima;
    private final String rotulo;
    private final String resposta;

    NivelSeguranca(double pontuacaoMinima, String rotulo, String resposta) {
        this.pontuacaoMinima = pontuacaoMinima;
        this.rotulo = rotulo;
        this.resposta = resposta;
    }

    /** Traduz uma pontuacao de 0 a 100 no nivel correspondente. */
    public static NivelSeguranca classificar(double pontuacao) {
        NivelSeguranca resultado = VERDE;
        for (NivelSeguranca nivel : values()) {
            if (pontuacao >= nivel.pontuacaoMinima) {
                resultado = nivel;
            }
        }
        return resultado;
    }

    public boolean exigeAlertaRemoto() {
        return this == LARANJA || this == VERMELHO;
    }

    public double getPontuacaoMinima() { return pontuacaoMinima; }
    public String getRotulo() { return rotulo; }
    public String getResposta() { return resposta; }
}
