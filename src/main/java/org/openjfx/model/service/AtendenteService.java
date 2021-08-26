package org.openjfx.model.service;

import org.openjfx.mapper.ColaboradorMapper;
import org.openjfx.model.dao.AtendenteDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dto.ColaboradorDTO;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;

import java.util.List;

public class AtendenteService {
    private AtendenteDao dao = DaoFactory.createAtendenteDao("Colaborador Service");
    ColaboradorMapper mapper = new ColaboradorMapper();

    public List<ColaboradorDTO> findAll() {

        return mapper.toDto(dao.findAll());
    }

    public List<ColaboradorDTO> findAllAtivos() {
        return mapper.toDto(dao.findAllAtivos());
    }

    public void saveOrUpdate(Atendente atendente) {
        if (atendente.getIdAtendente() == null) {
            dao.insert(atendente);
        } else {
            dao.update(atendente);
        }
    }

    public void remove(Atendente atendente) {
        dao.deleteById(atendente.getIdAtendente());
    }

    public Atendente logar(Atendente atendente) {
        return dao.logar(atendente.getIdAtendente(), atendente.getSenha());
    }

    public List<ColaboradorDTO> findByName(String name) {
        return mapper.toDto(dao.findByName(name));
    }
}
