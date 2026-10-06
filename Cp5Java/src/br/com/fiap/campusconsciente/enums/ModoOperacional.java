package br.com.fiap.campusconsciente.enums;

/**
 * O contexto em que o campus esta operando. O fator multiplica a
 * pontuacao dos sensores de presenca: o mesmo evento vale menos durante
 * a aula e mais de madrugada.
 */
public enum ModoOperacional {
    AULAS(0.5, "Ha aula agendada na sala - movimento esperado"),
    FORA_DE_AULA(1.0, "Predio aberto, sem aula agendada - presenca possivel"),
    ESVAZIAMENTO(1.5, "Fim da aula noturna - predio esvaziando"),
    FIM_DE_SEMANA(1.5, "Sem aulas - movimento nao esperado"),
    FERIADO(2.5, "Feriado no calendario - campus fechado"),
    MADRUGADA(2.5, "Predio fechado - qualquer presenca e suspeita");

    private final double fator;
    private final String descricao;

    ModoOperacional(double fator, String descricao) {
        this.fator = fator;
        this.descricao = descricao;
    }

    public double getFator() { return fator; }
    public String getDescricao() { return descricao; }
}
