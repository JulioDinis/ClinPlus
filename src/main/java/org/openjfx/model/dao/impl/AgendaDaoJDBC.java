package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.AgendaDao;

import java.sql.Connection;
import java.util.List;


public class AgendaDaoJDBC implements AgendaDao {

    private Connection connection;

    public AgendaDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(AgendaDao agendaDao) {

    }

    @Override
    public void update(AgendaDao agendaDao) {

    }

    @Override
    public void deleteById(Integer idAgenda) {

    }

    @Override
    public AgendaDao findById(Integer idAgenda) {
        return null;
    }

    @Override
    public List<AgendaDao> findAll() {
        return null;
    }
}
