package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.CaixaMensalDao;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.CaixaMensal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CaixaMensalDaoJDBC implements CaixaMensalDao {
    private Connection connection;

    public CaixaMensalDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(CaixaMensal caixaMensal) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO caixa_mensal "
                            + "(mes, ano, saldo_inicial) "
                            + "VALUES (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            createQuery(caixaMensal, statement);
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

    private synchronized PreparedStatement createQuery(CaixaMensal caixaMensal, PreparedStatement statement) {
        try {
            statement.setInt(1, caixaMensal.getMes());
            statement.setInt(2, caixaMensal.getAno());
            statement.setDouble(3, caixaMensal.getSaldoInicial());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(CaixaMensal caixaMensal) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE caixa_mensal"
                            + " SET saldo_final = ?,status = ? "
                            + "WHERE mes = ? and ano = ?");

            try {
                statement.setDouble(1, caixaMensal.getSaldoFinal());
                statement.setString(2, caixaMensal.getStatus());
                statement.setInt(3, caixaMensal.getMes());
                statement.setInt(4, caixaMensal.getAno());
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
    public void deleteById(Integer mes, Integer ano) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM caixa_mensal "
                            + "WHERE mes= ? and ano = ?");
            statement.setInt(1, mes);
            statement.setInt(2, ano);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public CaixaMensal findById(Integer mes, Integer ano) {
        PreparedStatement statement = null;
        List<CaixaMensal> list = new ArrayList<>();
        ResultSet rs = null;
        CaixaMensal caixaMensal = new CaixaMensal();
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM caixa_mensal " +
                            " WHERE mes = ? and ano = ? ");
            statement.setInt(1, mes);
            statement.setInt(2, ano);
            rs = statement.executeQuery();
            if (rs.next()) {
                caixaMensal = instantiateCaixaMensal(rs);
                return caixaMensal;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
            return caixaMensal;
        }
    }

    @Override
    public List<CaixaMensal> findAll() {
        PreparedStatement statement = null;
        List<CaixaMensal> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM caixa_mensal ORDER BY mes, ano");
            rs = statement.executeQuery();
            System.out.println(list);
            while (rs.next()) {
                CaixaMensal caixaMensal = instantiateCaixaMensal(rs);
                list.add(caixaMensal);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
            return list;
        }
    }

    private CaixaMensal instantiateCaixaMensal(ResultSet rs) {
        try {
            CaixaMensal novaCaixaMensal = new CaixaMensal(rs.getInt('1'),
                    rs.getInt(2),
                    rs.getDouble(3),
                    rs.getDouble(4),
                    rs.getString(5));
            return novaCaixaMensal;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }
}
