package org.openjfx.model.service;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.PacienteDao;
import org.openjfx.model.entities.Paciente;

/**
 * @author julio
 */
public class PacienteService {

    private PacienteDao dao = DaoFactory.createPacienteDao("Paciente Service");

    public List<Paciente> findAll() {

        return dao.findAll();
    }

    public List<Paciente> findAllAtivos() {
        return dao.findAllAtivos();
    }

    public void saveOrUpdate(Paciente paciente) {

        if (paciente.getIdPaciente() == null) {
            dao.insert(paciente);
        } else {
            dao.update(paciente);
        }

    }

    public void remove(Paciente obj) {
        dao.deleteById(obj.getIdPaciente());
    }

    public List<Paciente> findByName(String name) {
        return dao.findByName(name);
    }
    public Paciente findById(Integer id){
        return dao.findById(id);
    }
}



