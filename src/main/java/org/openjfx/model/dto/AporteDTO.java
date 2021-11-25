package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AporteDTO {
    private Integer id;
    private Double valor;
    private String descricao;
    private Date data;
    private Colaborador colaborador;
    private String colaboradorNome;
    private CaixaMensal caixaMensal;
}
