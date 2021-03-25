package org.openjfx.model.service;

import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.FuncionarioDao;
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Funcionario;

import java.util.List;

/**
 * @author julio
 */
public class FuncionarioService {

    private FuncionarioDao dao = DaoFactory.createFuncionarioDao("Funcionario Service");

    public List<Funcionario> findAll() {

        return dao.findAll();
    }

    public List<Funcionario> findAllAtivos() {
        return dao.findAllAtivos();
    }

    public void saveOrUpdate(Funcionario funcionario) {
        System.out.println("Chegou aqui");
        if (funcionario.getIdFuncionario() == null) {
            System.out.println("Chegou no insert");
            dao.insert(funcionario);
        } else {
            dao.update(funcionario);
        }

    }

    public void remove(Funcionario obj) {
        dao.deleteById(obj.getIdFuncionario());
    }
    public Funcionario logar(Funcionario funcionario){
        return dao.logar(funcionario.getIdFuncionario(), funcionario.getSenha());
    }
}
