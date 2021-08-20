package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Colaborador;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedimentoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idProcedimento;
    private String descricao;
    private Double valor;
    private Colaborador colaborador;
    private  String nomeEspecialista;
}
