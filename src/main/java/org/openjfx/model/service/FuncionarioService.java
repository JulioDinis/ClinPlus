package org.openjfx.model.service;

import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.FuncionarioDao;
import org.openjfx.model.entities.Colaborador;

import java.util.List;

/**
 * @author julio
 */
public class FuncionarioService {

    private FuncionarioDao dao = DaoFactory.createFuncionarioDao("Colaborador Service");

    public List<Colaborador> findAll() {

        return dao.findAll();
    }

    public List<Colaborador> findAllAtivos() {
        return dao.findAllAtivos();
    }

    public void saveOrUpdate(Colaborador colaborador) {
        System.out.println("Chegou aqui");
        if (colaborador.getIdFuncionario() == null) {
            System.out.println("Chegou no insert");
            dao.insert(colaborador);
        } else {
            dao.update(colaborador);
        }

    }

    public void remove(Colaborador obj) {
        dao.deleteById(obj.getIdFuncionario());
    }
    public Colaborador logar(Colaborador colaborador){
        return dao.logar(colaborador.getIdFuncionario(), colaborador.getSenha());
    }
}
