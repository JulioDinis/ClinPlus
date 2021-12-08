package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItensTratamentoDTO {

    private String descricao;
    private Tratamento tratamento;
    private Integer nrItem;
    private Procedimento procedimento;
    private Integer quantidade;
    private Date dataExecucao;
    private Double valor;
    private boolean executado;

    public ItensTratamentoDTO(Procedimento procedimento, Tratamento tratamento, Integer quantidade) {
        this.procedimento = procedimento;
        this.quantidade = quantidade;
        this.tratamento = tratamento;
        this.valor = procedimento.getValor();
        this.descricao = procedimento.getDescricao();
    }
    public void setExecutado(){
        if(dataExecucao != null){
            executado = true;
        }else{
            executado = false;
        }
    }
}
