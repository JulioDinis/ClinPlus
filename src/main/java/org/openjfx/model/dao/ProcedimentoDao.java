package org.openjfx.model.dao;

import org.openjfx.model.entities.Procedimento;

import java.util.List;

public interface ProcedimentoDao {
    void insert(Procedimento Procedimento);
    void update(Procedimento Procedimento);
    void deleteById(Integer idProcedimento);
    Procedimento findById(Integer idProcedimento);
    List<Procedimento> findAll();
    List<Procedimento> findAllAtivos();

    List<Procedimento> findByEspecilistaId(Integer id);
    List<Procedimento> findByDescricao(String descricao);
    List<Procedimento> findByDescricaoAndId(String descricao, Integer id);
}
