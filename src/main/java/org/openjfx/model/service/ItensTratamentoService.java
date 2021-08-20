package org.openjfx.model.service;

import org.openjfx.mapper.ItensTratamentoMapper;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.ItensTratamento;

import java.util.List;

public class ItensTratamentoService {

    private ItensTratamentoDao dao = DaoFactory.createItensTratamentoDao("ItensTratamento Service");

    private ItensTratamentoMapper mapper = new ItensTratamentoMapper();


    public List<ItensTratamentoDTO> findAll() {

        return mapper.toDto(dao.findAll());
    }



    public void saveOrUpdate(ItensTratamentoDTO itensTratamentoDto) {

        ItensTratamento it = mapper.toEntity(itensTratamentoDto);

//        dao.insert(it);

        if (itensTratamentoDto.getQuantidade() == 1) {
            dao.insert(mapper.toEntity(itensTratamentoDto));
        } else {
            dao.update(mapper.toEntity(itensTratamentoDto));
        }

    }

    public void remove(ItensTratamentoDTO obj) {
        dao.deleteById(obj.getNrItem());
    }

    public List<ItensTratamentoDTO> findByDescricao(String name) {
        return dao.findByDescricao(name);
    }


    public List<ItensTratamentoDTO> findByTratamentoId(Integer idTratamento) {
        return dao.findByTratamentoId(idTratamento);
    }

    public ItensTratamentoDTO findByTratamentoIdAndProcedimentoId(Integer idTratamento, Integer idProcedimento) {
        return  mapper.toDto(dao.findByTratamentoIdAndProcedimentoId(idTratamento, idProcedimento));
    }
}
