package br.com.fiap.campusconsciente.atuadores;

import br.com.fiap.campusconsciente.enums.NivelSeguranca;
import br.com.fiap.campusconsciente.modelos.Evento;

/** Fita de LED no ambiente: mostra o nivel em cores; pisca no vermelho. */
public class FitaLed extends Atuador {
    private int quantidadeLeds;
    private NivelSeguranca corAtual;

    public FitaLed() {
        super("Fita de LED");
        this.quantidadeLeds = 60;
        this.corAtual = NivelSeguranca.VERDE;
    }

    public FitaLed(String nome, int quantidadeLeds) {
        super(nome);
        this.quantidadeLeds = quantidadeLeds;
        this.corAtual = NivelSeguranca.VERDE;
    }

    @Override
    public void reagir(NivelSeguranca nivel, Evento evento) {
        if (!isHabilitado()) return;
        if (nivel != corAtual) {
            corAtual = nivel;
            registrarAcionamento();
        }
        String modo = nivel == NivelSeguranca.VERMELHO ? " PISCANDO" : "";
        System.out.println("   [LED]     " + quantidadeLeds + " LEDs em " + nivel + modo);
    }

    public int getQuantidadeLeds() { return quantidadeLeds; }
    public NivelSeguranca getCorAtual() { return corAtual; }
}
