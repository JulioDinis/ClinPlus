package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Aporte implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Double valor;
    private String descricao;
    private Date data;
    private Colaborador colaborador;
    private CaixaMensal caixaMensal;
}
