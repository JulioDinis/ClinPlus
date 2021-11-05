package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Agenda implements Serializable, Comparable<Agenda> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date data;
    private Time horario;
    private Paciente paciente;
    private Colaborador especialista;
    private String observacao;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agenda)) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(getData(), agenda.getData()) && Objects.equals(getHorario(), agenda.getHorario()) && Objects.equals(getEspecialista(), agenda.getEspecialista());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getData(), getHorario(), getPaciente(), getEspecialista());
    }

    @Override
    public int compareTo(@NotNull Agenda o) {
        return (this.getHorario().compareTo(o.getHorario()));
    }
}

