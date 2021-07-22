package org.openjfx.model.service;

import org.openjfx.mapper.TratamentoMapper;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.TratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.dto.TratamentoDTO;
import org.openjfx.model.entities.ItensTratamento;
import org.openjfx.model.entities.Procedimento;

import java.util.List;

public class TratamentoService {
    private TratamentoDao dao = DaoFactory.createTratamentoDao("Tratamento Service");
    private ItensTratamentoService itensTratamentoService = new ItensTratamentoService();
    private TratamentoMapper mapper = new TratamentoMapper();

    public List<TratamentoDTO> findAll() {
        return mapper.toDto(dao.findAll());
    }


    public int saveOrUpdate(TratamentoDTO tratamentoDTO) {
        System.out.println("** SALVAR **");
        int primaryKey;
        if (tratamentoDTO.getIdTratamento() == null) {
            primaryKey = dao.insert(mapper.toEntity(tratamentoDTO));
        } else {
            primaryKey = dao.update(mapper.toEntity(tratamentoDTO));
        }
        return primaryKey;
    }

    public void inserirProcedimento(Procedimento procedimento){

    }

    public void remove(TratamentoDTO tratamentoDTO) {
        dao.deleteById(tratamentoDTO.getIdTratamento());
    }

    public TratamentoDTO findById(Integer idTratamento) {
        return mapper.toDto(dao.findById(idTratamento));
    }
}
