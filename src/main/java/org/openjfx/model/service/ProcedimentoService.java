package org.openjfx.model.service;

import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.PacienteDao;
import org.openjfx.model.dao.ProcedimentoDao;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Procedimento;

import java.util.List;

/**
 * @author julio
 */
public class ProcedimentoService {

    private ProcedimentoDao dao = DaoFactory.createProcedimentoDao("Procedimento Service");

    public List<Procedimento> findAll() {

        return dao.findAll();
    }

    public List<Procedimento> findAllAtivos() {
        return dao.findAll();
    }

    public void saveOrUpdate(Procedimento Procedimento) {

        if (Procedimento.getIdProcedimento() == null) {
            dao.insert(Procedimento);
        } else {
            dao.update(Procedimento);
        }

    }

    public void remove(Procedimento obj) {
        dao.deleteById(obj.getIdProcedimento());
    }

    public List<Procedimento> findByEspecialista(Integer id) {
        return dao.findByEspecilistaId(id);
    }

    public List<Procedimento> findByDescricao(String descricao) {
        return dao.findByDescricao(descricao);
    }

    public List<Procedimento> findByDescricaoAndId(String descricao, Integer id) {
        return dao.findByDescricaoAndId(descricao, id);
    }

    public Procedimento findById(int id_procedimento) {
        return dao.findById(id_procedimento);
    }
}
