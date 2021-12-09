package org.openjfx.model.service;

import org.openjfx.mapper.PagamentoMapper;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.PagamentoDao;
import org.openjfx.model.dto.PagamentoDTO;

import java.util.List;

public class PagamentoService {
    private final PagamentoDao dao = DaoFactory.createPagamentoDao("Pagamento Service");
    private final PagamentoMapper mapper = new PagamentoMapper();

    public void saveOrUpdate(PagamentoDTO pagamentoDTO) {
        if (pagamentoDTO.getTratamento() == null || pagamentoDTO.getNumeroParcela()==null) {
            dao.insert(mapper.toEntity(pagamentoDTO));
        } else {
            dao.update(mapper.toEntity(pagamentoDTO));
        }
    }

    public List<PagamentoDTO> findAll() {
        return mapper.toDto(dao.findAll());
    }

    public PagamentoDTO findById(int pagamentoId) {
        return mapper.toDto(dao.findById(pagamentoId));
    }

    public void remove(PagamentoDTO pagamentoDTO) {
        if (pagamentoDTO != null) {
            dao.deleteById(mapper.toEntity(pagamentoDTO));
        }
    }

}
