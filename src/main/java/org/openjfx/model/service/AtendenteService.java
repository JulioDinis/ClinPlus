package org.openjfx.model.service;

import org.openjfx.model.dao.AtendenteDao;
import org.openjfx.model.dao.ColaboradorDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;

import java.util.List;

public class AtendenteService {
    private AtendenteDao dao = DaoFactory.createAtendenteDao("Colaborador Service");

    public List<Atendente> findAll() {

        return dao.findAll();
    }

    public List<Atendente> findAllAtivos() {
        return dao.findAllAtivos();
    }

    public void saveOrUpdate(Atendente atendente) {
        if (atendente.getIdFuncionario() == null) {
            dao.insert(atendente);
        } else {
            dao.update(atendente);
        }
    }

    public void remove(Atendente atendente) {
        dao.deleteById(atendente.getIdFuncionario());
    }

    public Atendente logar(Atendente atendente) {
        return dao.logar(atendente.getIdFuncionario(), atendente.getSenha());
    }

    public List<Atendente> findByName(String name) {
        return dao.findByName(name);
    }
}
