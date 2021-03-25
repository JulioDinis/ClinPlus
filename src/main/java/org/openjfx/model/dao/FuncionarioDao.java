package org.openjfx.model.dao;

import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Paciente;

import java.util.List;

public interface FuncionarioDao {
    void insert(Funcionario funcionario);

    void update(Funcionario funcionario);

    void deleteById(Integer idFuncionario);

    Funcionario findById(Integer idFuncionario);

    List<Funcionario> findAll();

    List<Funcionario> findAllAtivos();

    Funcionario logar(Integer idFuncionario, String senha);

}
