package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

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
