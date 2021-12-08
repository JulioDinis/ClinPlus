package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItensTratamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tratamento tratamento;
    private Integer nrItem;
    private Procedimento procedimento;
    private Integer quantidade;
    private Date dataExecucao;
    private Double valor;

    public ItensTratamento(Procedimento procedimento, Integer quantidade) {
        this.procedimento = procedimento;
        this.quantidade = quantidade;
        this.valor = procedimento.getValor();
    }
}
