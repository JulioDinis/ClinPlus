package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.CaixaMensalMapper;
import org.openjfx.model.dao.ContaDao;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Conta;
import org.openjfx.model.service.FinanceiroService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDaoJDBC implements ContaDao {
    private Connection connection;

    public ContaDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(Conta conta) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO conta "
                            + "(descricao, valor, data_vencimento, obs, mes, ano ) "
                            + "VALUES (?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            createQuery(conta, statement);
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

    private synchronized PreparedStatement createQuery(Conta conta, PreparedStatement statement) {
        try {
            statement.setString(1, conta.getDescricao());
            statement.setDouble(2, conta.getValor());
            statement.setDate(3, conta.getDataVencimento());
            statement.setString(4, conta.getObservacao());
            statement.setInt(5, conta.getCaixaMensal().getMes());
            statement.setInt(6, conta.getCaixaMensal().getAno());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Conta conta) {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE conta"
                            + " SET descricao= ?, " +
                            "valor= ?, " +
                            "data_vencimento= ?, " +
                            "obs= ?, " +
                            "data_pagamento= ?, " +
                            "valor_pagamento= ?,  " +
                            "mes =?, " +
                            "ano=? "
                            + "WHERE id_conta=?");

            try {

                statement.setString(1, conta.getDescricao());
                statement.setDouble(2, conta.getValor());
                statement.setDate(3, conta.getDataVencimento());
                statement.setString(4, conta.getObservacao());
                if (conta.getValorPagamento() != null) {
                    statement.setDate(5, conta.getDataPagamento());
                    statement.setDouble(6, conta.getValorPagamento());
                }else{
                    statement.setNull(5,Types.DATE);
                    statement.setNull(6,Types.DOUBLE);
                }
                statement.setInt(7, conta.getCaixaMensal().getMes());
                statement.setInt(8, conta.getCaixaMensal().getAno());
                statement.setInt(9, conta.getIdConta());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer idConta) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM conta "
                            + "WHERE id_conta = ?;");
            statement.setInt(1, idConta);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Conta findById(Integer idConta) {
        PreparedStatement statement = null;
        Conta conta = new Conta();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM conta " +
                            "WHERE id_conta =? " +
                            "ORDER BY data_cadastro");
            statement.setInt(1, idConta);
            rs = statement.executeQuery();
            if (rs.next()) {
                conta = instantiateConta(rs);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
            return conta;
        }
    }

    @Override
    public List<Conta> findAll() {
        PreparedStatement statement = null;
        List<Conta> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM conta ORDER BY data_cadastro");
            rs = statement.executeQuery();
            System.out.println(rs.toString());
            while (rs.next()) {
                Conta conta = instantiateConta(rs);
                list.add(conta);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
            return list;
        }
    }

    @Override
    public List<Conta> findByCaixaMensal(CaixaMensal caixaMensal) {
        PreparedStatement statement = null;
        List<Conta> contas = new ArrayList<>();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM conta " +
                            "WHERE mes =? " +
                            "and ano =? " +
                            "ORDER BY data_cadastro");
            statement.setInt(1, caixaMensal.getMes());
            statement.setInt(2, caixaMensal.getAno());
            rs = statement.executeQuery();
            while (rs.next()) {
                Conta conta = instantiateConta(rs);
                contas.add(conta);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
            return contas;
        }
    }

    private Conta instantiateConta(ResultSet rs) {
        FinanceiroService financeiroService = new FinanceiroService();
        CaixaMensalMapper mapper = new CaixaMensalMapper();
        Conta novaConta = new Conta();
        try {
            novaConta = new Conta(
                    rs.getInt("id_conta"),
                    rs.getDate("data_cadastro"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    rs.getDate("data_vencimento"),
                    rs.getDate("data_pagamento"),
                    rs.getDouble("valor_pagamento"),
                    rs.getString("obs"),
                    mapper.toEntity(financeiroService.buscarCaixaById(rs.getInt(9), rs.getInt(10)))
            );
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return novaConta;
    }
}
