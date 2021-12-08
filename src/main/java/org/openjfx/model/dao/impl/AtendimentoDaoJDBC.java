package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.AtendimentoDao;
import org.openjfx.model.entities.Atendimento;

import java.sql.*;
import java.util.List;

public class AtendimentoDaoJDBC implements AtendimentoDao {
    private final Connection connection;

    public AtendimentoDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Atendimento atendimento) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO atendimento "
                            + "(agendamento, anotações, tratamento) "
                            + "VALUES (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, atendimento.getAgendamento().getId());
            statement.setString(2, atendimento.getAnotacoes());
            statement.setInt(3, atendimento.getTratamento().getIdTratamento());
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
    public void update(Atendimento atendimento) {

    }

    @Override
    public List<Atendimento> findByPacienteId(Integer idPaciente) {
        return null;
    }

    @Override
    public List<Atendimento> findAll() {
        return null;
    }

    @Override
    public List<Atendimento> findByEspecialista(Integer idEspecialista) {
        return null;
    }
}
