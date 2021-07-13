package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItensTratamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tratamento tratamento;
    private Integer nrItem;
    private Procedimento procedimento;
    private Integer quantidade;
    private LocalDate dataExecucao;
    private Double valor;
}
