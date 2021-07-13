package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PagamentoOrcamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tratamento tratamento;
    private Integer numeroParcela;
    private Double valor;
    private LocalDate dataPagamento;
    private char formaPagamento;

}
