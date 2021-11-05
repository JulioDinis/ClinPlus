package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendaDTO {

    private Integer id;
    private Date data;
    private Time horario;
    private String paciente;
    private String observacao;
    private String status;

    private String especialista;
    private Integer idPaciente;
    private Integer idEspecialista;

    @Override
    public String toString() {
        return "\n********Agenda*********\n" +
                "\n data=" + data +
                "\n horario=" + horario +
                "\n paciente='" + paciente + '\'' +
                "\n especialista='" + especialista +
                "\n STATUS=>> " + status;
    }
}
