package org.openjfx.model.dao;

import org.openjfx.model.entities.Paciente;

import java.util.List;

public interface PacienteDao {
    void insert(Paciente paciente);
    void update(Paciente paciente);
    void deleteById(Integer idPaciente);
    Paciente findById(Integer idPaciente);
    List<Paciente> findAll();
    List<Paciente> findAllAtivos();
}
