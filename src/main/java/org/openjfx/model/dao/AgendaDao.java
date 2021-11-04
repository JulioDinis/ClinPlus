package org.openjfx.model.dao;

import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Colaborador;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public interface AgendaDao {
    void insert(Agenda agenda);

    void update(Agenda agenda);

    void deleteById(Integer idAgenda);

    Agenda findById(Integer idAgenda);
    List<Agenda> findByDate(Date data);

    List<Agenda> findAll();


    List<Agenda> findByDateAndEspecialista(Date data, Colaborador especialista);
}
