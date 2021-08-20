package org.openjfx.model.service;

import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.ColaboradorDao;
import org.openjfx.model.entities.Colaborador;

import java.util.List;

/**
 * @author julio
 */
public class ColaboradorService {

    private ColaboradorDao dao = DaoFactory.createFuncionarioDao("Colaborador Service");

    public List<Colaborador> findAll() {

        return dao.findAll();
    }

    public List<Colaborador> findAllAtivos() {
        return dao.findAllAtivos();
    }

    public void saveOrUpdate(Colaborador colaborador) {
        if (colaborador.getIdEspecialista() == null) {
            dao.insert(colaborador);
        } else {
            dao.update(colaborador);
        }
    }

    public void remove(Colaborador obj) {
        dao.deleteById(obj.getIdEspecialista());
    }
    public Colaborador logar(Colaborador colaborador){
        return dao.logar(colaborador.getIdEspecialista(), colaborador.getSenha());
    }

    public List<Colaborador> findByName(String name) {
        return dao.findByName(name);
    }

    public Colaborador findById(int id_colaborador) {
        return dao.findById(id_colaborador);
    }
}
