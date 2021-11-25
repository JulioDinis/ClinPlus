package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.CaixaMensal;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {
    private Integer idConta;
    private Date dataCadastro;
    private String descricao;
    private Double valor;
    private Date dataVencimento;
    private Date dataPagamento;
    private Double valorPagamento;
    private String observacao;
    private CaixaMensal caixaMensal;
}
