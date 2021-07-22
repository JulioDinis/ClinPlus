package org.openjfx.model.service;

import org.openjfx.mapper.ItensTratamentoMapper;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dao.PacienteDao;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.ItensTratamento;
import org.openjfx.model.entities.Paciente;

import java.util.List;

public class ItensTratamentoService {

    private ItensTratamentoDao dao = DaoFactory.createItensTratamentoDao("ItensTratamento Service");

    private ItensTratamentoMapper mapper = new ItensTratamentoMapper();


    public List<ItensTratamentoDto> findAll() {

        return mapper.toDto(dao.findAll());
    }



    public void saveOrUpdate(ItensTratamentoDto itensTratamentoDto) {

        ItensTratamento it = mapper.toEntity(itensTratamentoDto);

        dao.insert(it);

//        if (itensTratamentoDto.getTratamento() == null) {
//            dao.insert(mapper.toEntity(itensTratamentoDto));
//        } else {
//            dao.update(mapper.toEntity(itensTratamentoDto));
//        }

    }

    public void remove(ItensTratamentoDto obj) {
        dao.deleteById(obj.getNrItem());
    }

    public List<ItensTratamentoDto> findByDescricao(String name) {
        return dao.findByDescricao(name);
    }


    public List<ItensTratamentoDto> findByTratamentoId(Integer idTratamento) {
        return dao.findByTratamentoId(idTratamento);
    }
}
