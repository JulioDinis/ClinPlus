package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Tratamento;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PagamentoDTO {
    private Tratamento tratamento;
    private Integer numeroParcela;
    private Double valor;
    private Date dataPagamento;
    private String formaPagamento;
    private String paciente;
    private String especialista;

}
