package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Procedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idProcedimento;
    private String descricao;
    private Double valor;
    private Colaborador colaborador;

}
