package br.com.fiap.campusconsciente.modelos;

import br.com.fiap.campusconsciente.enums.ClasseObjeto;

/** Um objeto reconhecido pelo modelo de visao computacional num quadro. */
public class ObjetoDetectado {
    private ClasseObjeto classe;
    private double confianca;

    public ObjetoDetectado() {
        this(ClasseObjeto.SOMBRA, 0.0);
    }

    public ObjetoDetectado(ClasseObjeto classe, double confianca) {
        this.classe = classe;
        this.confianca = confianca;
    }

    public ClasseObjeto getClasse() { return classe; }
    public double getConfianca() { return confianca; }

    @Override
    public String toString() {
        return String.format("%s (%.0f%%)", classe.getDescricao(), confianca * 100);
    }
}
