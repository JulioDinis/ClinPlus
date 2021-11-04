package org.openjfx.model.dao;

import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.ItensTratamento;

import java.util.List;

public interface ItensTratamentoDao {
    void insert(ItensTratamento itensTratamento);
    void update(ItensTratamento itensTratamento);
    List<ItensTratamento> findAll();
    void deleteById(Integer nrItem);
    List<ItensTratamento> findByDescricao(String descricao);

    List<ItensTratamento> findByTratamentoId(Integer idTratamento);

    ItensTratamento findByTratamentoIdAndProcedimentoId(Integer idTratamento, Integer idProcedimento);
}
