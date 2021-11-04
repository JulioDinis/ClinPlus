package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador extends Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer idColaborador;
    private String conselhoRegional;
    private String especialidade;
    private String senha;
    private Date dataContrato;
    private Date dataDesligamento;

    @Override
    public String toString() {
        return especialidade + " - " + getNome();
    }
}
