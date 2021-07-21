package org.openjfx.model.dao;

import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.ItensTratamento;

import java.util.List;

public interface ItensTratamentoDao {
    void insert(ItensTratamento itensTratamentoDao);
    void update(ItensTratamento itensTratamentoDao);
    List<ItensTratamento> findAll();
    void deleteById(Integer nrItem);
    List<ItensTratamentoDto> findByDescricao(String descricao);
}
