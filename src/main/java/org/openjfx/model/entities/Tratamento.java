package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tratamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTratamento;
    private Double total;
    private Double desconto;
    private LocalDate dataOrcamento;
    private LocalDate validadeOrcamento;
    private Boolean status;
    private Integer quantidadeParcelas;
    private LocalDate dataAprovacao;
    private Paciente paciente;
}
