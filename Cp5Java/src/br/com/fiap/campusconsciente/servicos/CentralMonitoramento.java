package br.com.fiap.campusconsciente.servicos;

import br.com.fiap.campusconsciente.atuadores.Atuador;
import br.com.fiap.campusconsciente.enums.NivelSeguranca;
import br.com.fiap.campusconsciente.modelos.Evento;
import br.com.fiap.campusconsciente.modelos.Sensor;

/**
 * O servidor de borda: recebe os eventos, pede a avaliacao ao motor,
 * guarda o historico e aciona todos os atuadores.
 */
public class CentralMonitoramento {
    private static final int CAPACIDADE = 100;
    private String nome;
    private Sensor[] sensores;
    private Atuador[] atuadores;
    private Evento[] historico;
    private int totalSensores;
    private int totalAtuadores;
    private int totalEventos;
    private MotorDeScore motor;

    public CentralMonitoramento() {
        this("Central Campus Consciente", new MotorDeScore());
    }

    public CentralMonitoramento(String nome, MotorDeScore motor) {
        this.nome = nome;
        this.motor = motor;
        this.sensores = new Sensor[10];
        this.atuadores = new Atuador[10];
        this.historico = new Evento[CAPACIDADE];
    }

    public void cadastrarSensor(Sensor sensor) {
        if (totalSensores < sensores.length) sensores[totalSensores++] = sensor;
    }

    public void cadastrarAtuador(Atuador atuador) {
        if (totalAtuadores < atuadores.length) atuadores[totalAtuadores++] = atuador;
    }

    /** Equivalente ao POST /eventos do servidor real. */
    public NivelSeguranca registrarEvento(Evento evento) {
        if (evento == null || !evento.getSensor().isAtivo()) return nivelAtual();
        motor.avaliar(evento, historico, totalEventos);
        guardar(evento);
        System.out.println(" > " + evento);
        System.out.println("   conta: " + evento.getDetalhe());
        for (int i = 0; i < totalAtuadores; i++) {
            atuadores[i].reagir(evento.getNivel(), evento);   // polimorfismo
        }
        return evento.getNivel();
    }

    private void guardar(Evento evento) {
        if (totalEventos == historico.length) {   // historico cheio: descarta o mais antigo
            System.arraycopy(historico, 1, historico, 0, historico.length - 1);
            totalEventos--;
        }
        historico[totalEventos++] = evento;
    }

    public NivelSeguranca nivelAtual() {
        return totalEventos == 0 ? NivelSeguranca.VERDE : historico[totalEventos - 1].getNivel();
    }

    public void imprimirResumo() {
        int[] porNivel = new int[NivelSeguranca.values().length];
        for (int i = 0; i < totalEventos; i++) {
            porNivel[historico[i].getNivel().ordinal()]++;
        }
        System.out.println("Sensores cadastrados: " + totalSensores);
        for (int i = 0; i < totalSensores; i++) System.out.println("  - " + sensores[i]);
        System.out.println("Eventos registrados: " + totalEventos);
        for (NivelSeguranca nivel : NivelSeguranca.values()) {
            System.out.println("  - " + nivel + ": " + porNivel[nivel.ordinal()]);
        }
        System.out.println("Acionamentos dos atuadores:");
        for (int i = 0; i < totalAtuadores; i++) {
            System.out.println("  - " + atuadores[i].getNome() + ": " + atuadores[i].getAcionamentos());
        }
    }

    public String getNome() { return nome; }
    public int getTotalEventos() { return totalEventos; }
    public MotorDeScore getMotor() { return motor; }
}
