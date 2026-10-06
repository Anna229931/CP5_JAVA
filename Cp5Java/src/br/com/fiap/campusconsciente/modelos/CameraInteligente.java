package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.ClasseObjeto;

/**
 * Camera IP com visao computacional. Simula o detector YOLO: recebe um
 * quadro com os objetos que o modelo reconheceu e conta as pessoas com
 * confianca suficiente. Nao identifica ninguem - so conta (LGPD).
 */
public class CameraInteligente extends SensorDePresenca {
    public static final double PONTOS_BASE = 35;
    private double confiancaMinima;
    private int quadrosAnalisados;

    public CameraInteligente() {
        super("CAM-00", "teto da sala", PONTOS_BASE);
        this.confiancaMinima = 0.5;
    }

    public CameraInteligente(String id, String localizacao, double confiancaMinima) {
        super(id, localizacao, PONTOS_BASE);
        this.confiancaMinima = confiancaMinima;
    }

    /** Conta as pessoas detectadas no quadro acima da confianca minima. */
    public int analisarQuadro(Quadro quadro) {
        quadrosAnalisados++;
        int pessoas = 0;
        ObjetoDetectado[] objetos = quadro.getObjetos();
        for (ObjetoDetectado objeto : objetos) {
            if (objeto.getClasse() == ClasseObjeto.PESSOA && objeto.getConfianca() >= confiancaMinima) {
                pessoas++;
            }
        }
        return pessoas;
    }

    /** Analisa o quadro e gera um evento apenas se houver pessoa. */
    public Evento capturar(Quadro quadro) {
        int pessoas = analisarQuadro(quadro);
        if (pessoas == 0) {
            return null;
        }
        return new Evento(this, pessoas, quadro.getMomento());
    }

    @Override
    public String getTipo() {
        return "Camera com visao computacional";
    }

    public double getConfiancaMinima() { return confiancaMinima; }
    public int getQuadrosAnalisados() { return quadrosAnalisados; }

    public void setConfiancaMinima(double confiancaMinima) {
        if (confiancaMinima > 0 && confiancaMinima <= 1) {
            this.confiancaMinima = confiancaMinima;
        }
    }
}
