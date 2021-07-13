package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.AporteDao;

import java.sql.Connection;
import java.util.List;

public class AporteDaoJDBC implements AporteDao {

    private Connection connection;

    public AporteDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(AporteDao colaborador) {

    }

    @Override
    public void update(AporteDao colaborador) {

    }

    @Override
    public void deleteById(Integer idAporte) {

    }

    @Override
    public AporteDao findById(Integer idAporte) {
        return null;
    }

    @Override
    public List<AporteDao> findAll() {
        return null;
    }
}
