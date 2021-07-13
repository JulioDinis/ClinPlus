package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.AgendaDao;
import org.openjfx.model.dao.ContaDao;

import java.sql.Connection;
import java.util.List;


public class ContaDaoJDBC implements ContaDao {

    private Connection connection;

    public ContaDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }


    @Override
    public void insert(ContaDao agendaDao) {

    }

    @Override
    public void update(ContaDao agendaDao) {

    }

    @Override
    public void deleteById(Integer idAgenda) {

    }

    @Override
    public ContaDao findById(Integer idAgenda) {
        return null;
    }

    @Override
    public List<ContaDao> findAll() {
        return null;
    }
}
