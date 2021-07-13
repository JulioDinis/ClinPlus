package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDateTime dataHora;
    private String tipo;
    private String observacao;
    private Paciente paciente;
    private Colaborador especialista;
    private Tratamento tratamento;

}
