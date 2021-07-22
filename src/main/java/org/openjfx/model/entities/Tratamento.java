package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tratamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTratamento;
    private Double total;
    private Double desconto;
    private Date dataOrcamento;
    private Date validadeOrcamento;
    private Boolean status;
    private Integer quantidadeParcelas;
    private Date dataAprovacao;
    private Paciente paciente;
}
