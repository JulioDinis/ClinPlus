package org.openjfx.model.dao;

import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Atendimento;

import java.util.List;

public interface AtendimentoDao {
    void insert(Atendimento atendimento);

    void update(Atendimento atendimento);

    List<Atendimento> findByPacienteId(Integer idPaciente);

    List<Atendimento> findAll();

    List<Atendimento> findByEspecialista(Integer idEspecialista);

}
