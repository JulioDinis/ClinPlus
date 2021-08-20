package org.openjfx.model.dao;

import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;

import java.util.List;

public interface AtendenteDao {
    void insert(Atendente atendente);

    void update(Atendente colaborador);

    void deleteById(Integer idFuncionario);

    Atendente findById(Integer idFuncionario);

    List<Atendente> findAll();

    List<Atendente> findAllAtivos();

    Atendente logar(Integer idFuncionario, String senha);

    List<Atendente> findByName(String name);
}
