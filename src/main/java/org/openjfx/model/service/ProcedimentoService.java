package org.openjfx.model.service;

import org.openjfx.mapper.ProcedimentoMapper;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.PacienteDao;
import org.openjfx.model.dao.ProcedimentoDao;
import org.openjfx.model.dto.ProcedimentoDTO;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Procedimento;

import java.util.List;

/**
 * @author julio
 */
public class ProcedimentoService {

    private ProcedimentoMapper mapper = new ProcedimentoMapper();

    private ProcedimentoDao dao = DaoFactory.createProcedimentoDao("Procedimento Service");

    public List<ProcedimentoDTO> findAll() {

        return mapper.toDto(dao.findAll());
    }

    public List<ProcedimentoDTO> findAllAtivos() {

        return mapper.toDto(dao.findAll());
    }

    public void saveOrUpdate(Procedimento procedimento) {
        if (procedimento.getIdProcedimento() == null) {
            dao.insert(procedimento);
        } else {
            dao.update(procedimento);
        }
    }

    public void remove(Procedimento procedimento) {
        dao.deleteById(procedimento.getIdProcedimento());
    }

    public List<ProcedimentoDTO> findByEspecialista(Integer id) {
        return mapper.toDto(dao.findByEspecilistaId(id));
    }

    public List<ProcedimentoDTO> findByDescricao(String descricao) {
        return mapper.toDto(dao.findByDescricao(descricao));
    }

    public List<ProcedimentoDTO> findByDescricaoAndId(String descricao, Integer id) {
        return mapper.toDto(dao.findByDescricaoAndId(descricao, id));
    }

    public ProcedimentoDTO findById(int id_procedimento) {
        return mapper.toDto(dao.findById(id_procedimento));
    }
}
