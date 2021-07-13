package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.model.dao.CaixaMensalDao;

import java.sql.Connection;
import java.util.List;

public class CaixaMensalDaoJDBC implements CaixaMensalDao {
    private Connection connection;

    public CaixaMensalDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(CaixaMensalDao caixaMensal) {

    }

    @Override
    public void update(CaixaMensalDao colaborador) {

    }

    @Override
    public void deleteById(Integer idFuncionario) {

    }

    @Override
    public List<CaixaMensalDao> findAll() {
        return null;
    }
}
