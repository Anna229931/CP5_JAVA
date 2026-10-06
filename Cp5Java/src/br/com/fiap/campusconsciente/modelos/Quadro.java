package br.com.fiap.campusconsciente.modelos;

import java.time.LocalDateTime;

/** Um quadro (frame) do video da camera, com os objetos detectados nele. */
public class Quadro {
    private int numero;
    private LocalDateTime momento;
    private ObjetoDetectado[] objetos;

    public Quadro() {
        this(0, LocalDateTime.now(), new ObjetoDetectado[0]);
    }

    public Quadro(int numero, LocalDateTime momento, ObjetoDetectado[] objetos) {
        this.numero = numero;
        this.momento = momento;
        this.objetos = objetos;
    }

    public int getNumero() { return numero; }
    public LocalDateTime getMomento() { return momento; }
    public ObjetoDetectado[] getObjetos() { return objetos; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Quadro #" + numero + ": ");
        for (int i = 0; i < objetos.length; i++) {
            sb.append(objetos[i]);
            if (i < objetos.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
