package org.openjfx.model.service;

import org.openjfx.mapper.ContaMapper;
import org.openjfx.model.dao.ContaDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.CaixaMensal;

import java.util.List;

public class ContaService {
    private ContaDao dao = DaoFactory.createContaDao("Conta Service");
    private ContaMapper mapper = new ContaMapper();

    public void saveOrUpdate(ContaDTO contaDTO) {
        if (contaDTO.getIdConta() == null) {
            dao.insert(mapper.toEntity(contaDTO));
        } else {
            dao.update(mapper.toEntity(contaDTO));
        }
    }

    public List<ContaDTO> findAll() {
        return mapper.toDto(dao.findAll());
    }
    public ContaDTO findById(ContaDTO contaDTO) {
        return mapper.toDto(dao.findById(contaDTO.getIdConta()));
    }
    public List<ContaDTO> findByCaixa(CaixaMensal caixaMensal){
        return mapper.toDto(dao.findByCaixaMensal(caixaMensal));
    }

    public void remove(ContaDTO contaDTO) {
        if(contaDTO != null){
            dao.deleteById(contaDTO.getIdConta());
        }
    }

}
