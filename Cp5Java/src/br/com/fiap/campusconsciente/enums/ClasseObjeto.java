package br.com.fiap.campusconsciente.enums;

/**
 * Classes de objeto que o modelo de visao computacional sabe reconhecer
 * num quadro de video. Apenas PESSOA interessa ao sistema de seguranca.
 */
public enum ClasseObjeto {
    PESSOA("pessoa"),
    CADEIRA("cadeira"),
    MONITOR("monitor"),
    MOCHILA("mochila"),
    SOMBRA("sombra");

    private final String descricao;

    ClasseObjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }
}
