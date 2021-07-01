package org.openjfx.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Procedimento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idProcedimento;
    private String descricao;
    private Double valor;
    private Integer idEspecialista;

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdProcedimento() {
        return idProcedimento;
    }

    public void setIdProcedimento(Integer idProcedimento) {
        this.idProcedimento = idProcedimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Procedimento() {
    }

}
