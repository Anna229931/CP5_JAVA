package br.com.fiap.campusconsciente;

import br.com.fiap.campusconsciente.atuadores.AlertaCelular;
import br.com.fiap.campusconsciente.atuadores.FitaLed;
import br.com.fiap.campusconsciente.atuadores.Sirene;
import br.com.fiap.campusconsciente.enums.ClasseObjeto;
import br.com.fiap.campusconsciente.modelos.CameraInteligente;
import br.com.fiap.campusconsciente.modelos.Evento;
import br.com.fiap.campusconsciente.modelos.ObjetoDetectado;
import br.com.fiap.campusconsciente.modelos.Quadro;
import br.com.fiap.campusconsciente.modelos.Sensor;
import br.com.fiap.campusconsciente.modelos.SensorPIR;
import br.com.fiap.campusconsciente.modelos.SensorTemperatura;
import br.com.fiap.campusconsciente.servicos.CentralMonitoramento;
import br.com.fiap.campusconsciente.servicos.GradeHorarios;
import br.com.fiap.campusconsciente.servicos.MotorDeScore;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simulador do Campus Consciente - grupo DataCore, Challenge FIAP x Daten.
 * Reproduz, em ordem cronologica, os cenarios que o sistema real resolve.
 */
public class Main {

    public static void main(String[] args) {
        // ---------- montagem do sistema ----------
        LocalDate[] feriados = {LocalDate.of(2026, 10, 12), LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 20)};
        GradeHorarios grade = new GradeHorarios("Lab. 302", new GradeHorarios().getAulas(), feriados);
        CentralMonitoramento central = new CentralMonitoramento("Central Lab. 302", new MotorDeScore(grade, 2));

        SensorPIR pir = new SensorPIR("PIR-01", "parede da porta (ponto cego da camera)", 7);
        CameraInteligente camera = new CameraInteligente("CAM-01", "canto superior da sala", 0.5);
        SensorTemperatura temperatura = new SensorTemperatura();   // construtor sem parametros
        central.cadastrarSensor(pir);
        central.cadastrarSensor(camera);
        central.cadastrarSensor(temperatura);

        central.cadastrarAtuador(new FitaLed());                   // construtor sem parametros
        central.cadastrarAtuador(new Sirene("Sirene do laboratorio", 2000));
        central.cadastrarAtuador(new AlertaCelular("Telegram", "seguranca do campus"));

        titulo("CAMPUS CONSCIENTE - simulador de seguranca contextual");
        System.out.println("Sensores criados: " + Sensor.getTotalSensores());

        // ---------- cenario 1: presenca esperada ----------
        titulo("1. Terca, 14h - aula acontecendo");
        Quadro aula = new Quadro(1, LocalDateTime.of(2026, 10, 6, 14, 0), new ObjetoDetectado[]{
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.94),
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.88),
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.71),
                new ObjetoDetectado(ClasseObjeto.CADEIRA, 0.97),
                new ObjetoDetectado(ClasseObjeto.MONITOR, 0.92)});
        processarQuadro(central, camera, aula);

        // ---------- cenario 2: falso positivo filtrado ----------
        titulo("2. Terca, 14h00m30s - sombra na imagem");
        Quadro sombra = new Quadro(2, LocalDateTime.of(2026, 10, 6, 14, 0, 30), new ObjetoDetectado[]{
                new ObjetoDetectado(ClasseObjeto.SOMBRA, 0.62),
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.38),
                new ObjetoDetectado(ClasseObjeto.MOCHILA, 0.80)});
        processarQuadro(central, camera, sombra);

        // ---------- cenario 3: superaquecimento ----------
        titulo("3. Quarta, 10h - gabinete esquentando");
        double[] leituras = {30, 50, 58, 68, 70};
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 7, 10, 0);
        for (int i = 0; i < leituras.length; i++) {
            central.registrarEvento(new Evento(temperatura, leituras[i], inicio.plusSeconds(i * 10L)));
        }

        // ---------- cenario 4: duas fontes confirmam ----------
        titulo("4. Quinta, 15h - sala sem aula agendada");
        central.registrarEvento(new Evento(pir, 1, LocalDateTime.of(2026, 10, 8, 15, 0)));
        Quadro quinta = new Quadro(3, LocalDateTime.of(2026, 10, 8, 15, 1), new ObjetoDetectado[]{
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.86)});
        processarQuadro(central, camera, quinta);

        // ---------- cenario 5: madrugada ----------
        titulo("5. Domingo, 3h12 - campus fechado");
        Quadro madrugada = new Quadro(4, LocalDateTime.of(2026, 10, 11, 3, 12), new ObjetoDetectado[]{
                new ObjetoDetectado(ClasseObjeto.PESSOA, 0.91),
                new ObjetoDetectado(ClasseObjeto.MOCHILA, 0.77)});
        processarQuadro(central, camera, madrugada);

        // ---------- cenario 6: feriado ----------
        titulo("6. Segunda, 15h - feriado de 12 de outubro");
        central.registrarEvento(new Evento(pir, 1, LocalDateTime.of(2026, 10, 12, 15, 0)));

        // ---------- resumo ----------
        titulo("RESUMO DA SIMULACAO");
        central.imprimirResumo();
        System.out.println("Quadros analisados pela camera: " + camera.getQuadrosAnalisados());
    }

    private static void processarQuadro(CentralMonitoramento central, CameraInteligente camera, Quadro quadro) {
        System.out.println(" " + quadro);
        Evento evento = camera.capturar(quadro);
        if (evento == null) {
            System.out.println("   nenhuma pessoa acima de " + (int) (camera.getConfiancaMinima() * 100)
                    + "% de confianca - nada registrado");
        } else {
            System.out.println("   pessoas detectadas: " + (int) evento.getValor());
            central.registrarEvento(evento);
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}
