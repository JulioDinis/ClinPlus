package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.PagamentoDao;
import org.openjfx.model.entities.Pagamento;

import java.sql.*;
import java.util.List;

public class PagamentoDaoJDBC implements PagamentoDao {

    private final Connection connection;

    public PagamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(Pagamento pagamento) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO pagamento_tratamento "
                            + "(tratamento, nr_parcela, valor, data_pagamento, forma_pagamento) "
                            + "VALUES (?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, pagamento.getTratamento().getIdTratamento());
            statement.setInt(2, pagamento.getNumeroParcela());
            statement.setDouble(3, pagamento.getValor());
            statement.setDate(4, new Date(pagamento.getDataPagamento().getTime()));
            statement.setString(5, pagamento.getFormaPagamento());

            Integer linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhum registo afetado");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Pagamento agendaDao) {

    }

    @Override
    public void deleteById(Pagamento pagamento) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM pagamento_tratamento "
                            + "WHERE tratamento = ? "
                            + "and nr_parcela = ? ");
            statement.setInt(1, pagamento.getTratamento().getIdTratamento());
            statement.setInt(2, pagamento.getNumeroParcela());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public List<Pagamento> findAll() {
        return null;
    }

    @Override
    public Pagamento findById(int pagamentoId) {
        return null;
    }
}
