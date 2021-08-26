package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorDTO {
    private static final long serialVersionUID = 1L;
    private Integer idAtendente;
    private String especialidade;
    private String senha;
    private Double salario;
    private Date dataContrato;
    private Date dataDesligamento;
    private Integer idFuncionario;
    private String funcao;

}
