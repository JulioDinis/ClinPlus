package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.TratamentoDao;

import java.sql.Connection;
import java.util.List;

public class TratamentoDaoJDBC implements TratamentoDao {
    private final Connection connection;

    public TratamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(TratamentoDao agendaDao) {

    }

    @Override
    public void update(TratamentoDao agendaDao) {

    }

    @Override
    public void deleteById(Integer idTratamento) {

    }

    @Override
    public TratamentoDao findById(Integer idTratamento) {
        return null;
    }

    @Override
    public List<TratamentoDao> findAll() {
        return null;
    }
}
