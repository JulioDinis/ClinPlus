package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItensTratamentoDto {
    private static final long serialVersionUID = 1L;
    private  String descricao;
    private Tratamento tratamento;
    private Integer nrItem;
    private Procedimento procedimento;
    private Integer quantidade;
    private LocalDate dataExecucao;
    private Double valor;

    public ItensTratamentoDto(Procedimento procedimento, Integer quantidade) {
        this.procedimento = procedimento;
        this.quantidade = quantidade;
        this.valor = procedimento.getValor();
        this.descricao = procedimento.getDescricao();
    }
}
