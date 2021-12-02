package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.TratamentoDao;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;

import java.sql.*;
import java.util.List;

public class TratamentoDaoJDBC implements TratamentoDao {
    private final Connection connection;

    public TratamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public int insert(Tratamento tratamento) {
        PreparedStatement statement = null;
        int key = 0;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO tratamento "
                            + "(total, desconto, validade_orcamento, status, paciente ) "
                            + "VALUES (?,?,?,'t',?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setDouble(1, tratamento.getTotal());
                statement.setDouble(2, tratamento.getDesconto());
                statement.setDate(3, new Date(tratamento.getValidadeOrcamento().getTime()));
                statement.setInt(4, tratamento.getPaciente().getIdPaciente());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DbException(e.getMessage());
            }
            Integer linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    // Retrieve the auto generated key(s).
                    key = rs.getInt(1);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhum registo afetado");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
        return key;
    }

    @Override
    public int update(Tratamento tratamento) {
        PreparedStatement statement = null;
        int key = 0;
        try {
            statement = connection.prepareStatement(
                    "UPDATE tratamento "
                            + " SET total = ?,"
                            + " desconto = ? "
                            + " WHERE tratamento.id_tratamento = ?",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setDouble(1, tratamento.getTotal());
                statement.setDouble(2, tratamento.getDesconto());
                statement.setInt(3, tratamento.getIdTratamento());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DbException(e.getMessage());
            }
            Integer linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    // Retrieve the auto generated key(s).
                    key = rs.getInt(1);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhum registo afetado");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
        return key;
    }

    @Override
    public void deleteById(Integer idTratamento) {

    }

    @Override
    public Tratamento findById(Integer idTratamento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM tratamento "
                            + "WHERE id_tratamento = ?");

            statement.setInt(1, idTratamento);
            rs = statement.executeQuery();
            if (rs.next()) {
                Tratamento tratamento = instantiateTratamento(rs);
                return tratamento;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private Tratamento instantiateTratamento(ResultSet rs) {
        Tratamento tratamento = new Tratamento();

        try {
            tratamento.setIdTratamento(rs.getInt("id_tratamento"));
            tratamento.setTotal(rs.getDouble("total"));
            tratamento.setDesconto(rs.getDouble("desconto"));
            tratamento.setDataOrcamento(rs.getDate("data_orcamento"));
            tratamento.setValidadeOrcamento(rs.getDate("validade_orcamento"));
//            tratamento.setAtendimento(rs.getDate("data_orcamento"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tratamento;
    }

    @Override
    public List<Tratamento> findAll() {
        return null;
    }

    private synchronized PreparedStatement createQuery(Tratamento tratamento, PreparedStatement statement) {
        return null;
    }
}
