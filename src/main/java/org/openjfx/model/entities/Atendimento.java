package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atendimento {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer idAtendimento;
    private Agenda agendamento;
    private String anotacoes;
    private Tratamento tratamento;
}
