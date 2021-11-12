package org.openjfx.model.entities;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaixaMensal implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer mes;
    private Integer ano;
    private Double saldoInicial;
    private Double saldoFinal;
    private String status;

    public CaixaMensal(Integer mes, Integer ano, Double saldoInicial) {
        this.mes = mes;
        this.ano = ano;
        this.saldoInicial = saldoInicial;
        this.status = "Aberto";
    }
}
