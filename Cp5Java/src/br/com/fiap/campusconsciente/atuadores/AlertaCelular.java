package br.com.fiap.campusconsciente.atuadores;

import br.com.fiap.campusconsciente.enums.NivelSeguranca;
import br.com.fiap.campusconsciente.modelos.Evento;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Mensagem no celular do responsavel. So para os niveis criticos, e com
 * anti-flood: o mesmo nivel nao gera nova mensagem antes de 5 minutos.
 */
public class AlertaCelular extends Atuador {
    public static final int INTERVALO_MINIMO_MINUTOS = 5;
    private String destinatario;
    private NivelSeguranca ultimoNivelEnviado;
    private LocalDateTime ultimoEnvio;
    private int mensagensSuprimidas;

    public AlertaCelular() {
        super("Alerta no celular");
        this.destinatario = "responsavel";
    }

    public AlertaCelular(String nome, String destinatario) {
        super(nome);
        this.destinatario = destinatario;
    }

    @Override
    public void reagir(NivelSeguranca nivel, Evento evento) {
        if (!isHabilitado() || !nivel.exigeAlertaRemoto()) return;
        if (nivel == ultimoNivelEnviado && ultimoEnvio != null
                && Duration.between(ultimoEnvio, evento.getMomento()).toMinutes() < INTERVALO_MINIMO_MINUTOS) {
            mensagensSuprimidas++;
            System.out.println("   [CELULAR] mesmo nivel ha menos de " + INTERVALO_MINIMO_MINUTOS
                    + " min - mensagem suprimida (anti-flood)");
            return;
        }
        ultimoNivelEnviado = nivel;
        ultimoEnvio = evento.getMomento();
        registrarAcionamento();
        System.out.printf("   [CELULAR] para %s: %s - %.1f/100 - sensor: %s%n",
                destinatario, nivel, evento.getPontuacao(), evento.getSensor().getTipo());
    }

    public String getDestinatario() { return destinatario; }
    public int getMensagensSuprimidas() { return mensagensSuprimidas; }
}
