package br.com.fiap.campusconsciente.atuadores;

import br.com.fiap.campusconsciente.enums.NivelSeguranca;
import br.com.fiap.campusconsciente.modelos.Evento;

/** Alarme sonoro: so toca no nivel critico. */
public class Sirene extends Atuador {
    private int frequenciaHz;

    public Sirene() {
        super("Sirene");
        this.frequenciaHz = 2000;
    }

    public Sirene(String nome, int frequenciaHz) {
        super(nome);
        this.frequenciaHz = frequenciaHz;
    }

    @Override
    public void reagir(NivelSeguranca nivel, Evento evento) {
        if (!isHabilitado() || nivel != NivelSeguranca.VERMELHO) return;
        registrarAcionamento();
        System.out.println("   [SIRENE]  tocando a " + frequenciaHz + " Hz");
    }

    public int getFrequenciaHz() { return frequenciaHz; }
}
