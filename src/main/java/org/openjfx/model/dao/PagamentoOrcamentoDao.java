package org.openjfx.model.dao;

import java.util.List;

public interface PagamentoOrcamentoDao {

    void insert(PagamentoOrcamentoDao agendaDao);

    void update(PagamentoOrcamentoDao agendaDao);

    void deleteById(Integer idAgenda);

    List<PagamentoOrcamentoDao> findAll();


}
