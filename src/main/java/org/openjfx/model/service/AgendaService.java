package org.openjfx.model.service;

import org.openjfx.mapper.AgendaMapper;
import org.openjfx.model.dao.AgendaDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Colaborador;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public class AgendaService {

    private AgendaDao dao = DaoFactory.createAgendaDao("Agenda Service!");
    private AgendaMapper mapper = new AgendaMapper();


    public List<AgendaDTO> findByData(Date data, Colaborador especialista ) {
        return mapper.toDto(dao.findByDate(data), data, especialista);
    }
    public List<AgendaDTO> findByDataAndEspecialista(Date data, Colaborador especialista ) {
        return mapper.toDto(dao.findByDateAndEspecialista(data, especialista), data, especialista);
    }



    public void saveOrUpdate(Agenda entity) {
        System.out.println("agenda service"+ entity);
        dao.insert(entity);
    }
}
