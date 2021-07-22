package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.ItensTratamento;

import java.sql.*;
import java.util.List;

public class ItensTratamentoDaoJDBC implements ItensTratamentoDao {
    private Connection connection;

    public ItensTratamentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public void insert(ItensTratamento itensTratamento) {

        System.out.println("ITENS ->>>> " + itensTratamento);
        PreparedStatement statement = null;
        int key = 0;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO itens_orcamento "
                            + "(orcamento, procedimento, quantidade) "
                            + "VALUES (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setInt(1, itensTratamento.getTratamento().getIdTratamento());
                statement.setInt(2, itensTratamento.getProcedimento().getIdProcedimento());
                statement.setInt(3, itensTratamento.getQuantidade());
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

    @Override
    public List<ItensTratamentoDto> findByTratamentoId(Integer idTratamento) {
        return null;
    }
}
