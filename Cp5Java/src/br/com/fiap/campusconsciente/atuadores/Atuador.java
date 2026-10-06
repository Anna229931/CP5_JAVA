package br.com.fiap.campusconsciente.atuadores;

import br.com.fiap.campusconsciente.enums.NivelSeguranca;
import br.com.fiap.campusconsciente.modelos.Evento;

/**
 * Classe mae de tudo o que responde a um nivel de seguranca. A central
 * chama reagir() em todos, sem saber qual e qual (polimorfismo).
 */
public abstract class Atuador {
    private String nome;
    private boolean habilitado;
    private int acionamentos;

    public Atuador() {
        this("Atuador generico");
    }

    public Atuador(String nome) {
        this.nome = nome;
        this.habilitado = true;
    }

    /** Cada atuador decide sozinho como responder ao nivel. */
    public abstract void reagir(NivelSeguranca nivel, Evento evento);

    protected void registrarAcionamento() { acionamentos++; }

    public void desabilitar() { this.habilitado = false; }
    public void habilitar() { this.habilitado = true; }

    public String getNome() { return nome; }
    public boolean isHabilitado() { return habilitado; }
    public int getAcionamentos() { return acionamentos; }
}
