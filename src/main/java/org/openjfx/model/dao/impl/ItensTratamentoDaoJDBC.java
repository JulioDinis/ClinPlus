package org.openjfx.model.dao.impl;

import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.ItensTratamento;

import java.sql.Connection;
import java.util.List;

public class ItensTratamentoDaoJDBC implements ItensTratamentoDao {
    private Connection connection;

    public ItensTratamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(ItensTratamento itensTratamentoDao) {

    }

    @Override
    public void update(ItensTratamento itensTratamentoDao) {

    }

    @Override
    public List<ItensTratamento> findAll() {
        return null;
    }

    @Override
    public void deleteById(Integer nrItem) {

    }

    @Override
    public List<ItensTratamentoDto> findByDescricao(String descricao) {
        return null;
    }
}
