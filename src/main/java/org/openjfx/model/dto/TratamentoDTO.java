package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Procedimento;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TratamentoDTO {
    private Integer idTratamento;
    private Double total;
    private Double desconto;
    private Date dataOrcamento;
    private Date validadeOrcamento;
    private String status;
    private Integer quantidadeParcelas;
    private Date dataAprovacao;
    private Paciente paciente;
    private Procedimento procedimento;
    private Colaborador especialista;
}
