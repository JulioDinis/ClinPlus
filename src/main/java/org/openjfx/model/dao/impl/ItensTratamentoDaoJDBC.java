package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.ItensTratamentoDao;

import java.sql.Connection;
import java.util.List;

public class ItensTratamentoDaoJDBC implements ItensTratamentoDao {
    private Connection connection;

    public ItensTratamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(ItensTratamentoDao itensTratamentoDao) {

    }

    @Override
    public void update(ItensTratamentoDao itensTratamentoDao) {

    }

    @Override
    public List<ItensTratamentoDao> findAll() {
        return null;
    }
}
