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
                    "INSERT INTO orcamento "
                            + "(total, desconto, validade_orcamento) "
                            + "VALUES (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setDouble(1, tratamento.getTotal());
                statement.setDouble(2, tratamento.getDesconto());
                statement.setDate(3, new Date(tratamento.getValidadeOrcamento().getTime()));
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
        return 1;
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
                            + "FROM orcamento "
                            + "WHERE id_orcamento = ?");

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
            tratamento.setIdTratamento(rs.getInt("id_orcamento"));
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
