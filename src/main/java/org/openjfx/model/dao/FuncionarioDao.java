package org.openjfx.model.dao;

import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;

import java.util.List;

public interface FuncionarioDao {
    void insert(Colaborador colaborador);

    void update(Colaborador colaborador);

    void deleteById(Integer idFuncionario);

    Colaborador findById(Integer idFuncionario);

    List<Colaborador> findAll();

    List<Colaborador> findAllAtivos();

    Colaborador logar(Integer idFuncionario, String senha);

    List<Colaborador> findByName(String name);
}
