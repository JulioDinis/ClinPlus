package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.PagamentoOrcamentoDao;

import java.sql.Connection;
import java.util.List;

public class PagamentoOrcamentoDaoJDBC implements PagamentoOrcamentoDao {

    private final Connection connection;

    public PagamentoOrcamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(PagamentoOrcamentoDao agendaDao) {

    }

    @Override
    public void update(PagamentoOrcamentoDao agendaDao) {

    }

    @Override
    public void deleteById(Integer idAgenda) {

    }

    @Override
    public List<PagamentoOrcamentoDao> findAll() {
        return null;
    }
}
