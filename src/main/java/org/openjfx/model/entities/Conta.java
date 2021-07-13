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
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idConta;
    private LocalDate dataCadastro;
    private String descricao;
    private Double valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Double valorPagamento;
    private String observacao;
    private CaixaMensal caixaMensal;
}
