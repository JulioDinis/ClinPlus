package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.TratamentoDao;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.PacienteService;

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
                            + "(total, desconto, validade_orcamento, status, paciente, especialista, quantidade_parcelas ) "
                            + "VALUES (?,?,?,'AGUARDANDO',?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setDouble(1, tratamento.getTotal());
                statement.setDouble(2, tratamento.getDesconto());
                statement.setDate(3, new Date(tratamento.getValidadeOrcamento().getTime()));
                statement.setInt(4, tratamento.getPaciente().getIdPaciente());
                statement.setInt(5, tratamento.getEspecialista().getIdColaborador());
                statement.setInt(6, tratamento.getQuantidadeParcelas() != null ? tratamento.getQuantidadeParcelas() : 1);
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
                            + " desconto = ?, "
                            + " quantidade_parcelas = ? "
                            + " WHERE tratamento.id_tratamento = ?",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setDouble(1, tratamento.getTotal());
                statement.setDouble(2, tratamento.getDesconto());
                statement.setDouble(3, tratamento.getQuantidadeParcelas());
                statement.setInt(4, tratamento.getIdTratamento());
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
        PacienteService pacienteService = new PacienteService();
        ColaboradorService especialistaService = new ColaboradorService();
        try {
            tratamento.setIdTratamento(rs.getInt("id_tratamento"));
            tratamento.setTotal(rs.getDouble("total"));
            tratamento.setDesconto(rs.getDouble("desconto"));
            tratamento.setDataOrcamento(rs.getDate("data_orcamento"));
            tratamento.setValidadeOrcamento(rs.getDate("validade_orcamento"));
            tratamento.setPaciente(pacienteService.findById(rs.getInt("paciente")));
            tratamento.setQuantidadeParcelas(rs.getInt("quantidade_parcelas"));
            tratamento.setEspecialista(especialistaService.findById(rs.getInt("especialista")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tratamento;
    }

    @Override
    public List<Tratamento> findAll() {
        return null;
    }

    @Override
    public Tratamento findOrcamentoAberto(Integer idPaciente, Integer idEspecialista) {
        //TODO Select * from Tratamento Where status = ABERTO and idPaciente and idEspecialista
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM tratamento "
                            + "WHERE paciente = ? "
                            + "and especialista =? "
                            + "and status = ? ;");

            statement.setInt(1, idPaciente);
            statement.setInt(2, idEspecialista);
            statement.setString(3, "AGUARDANDO");
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

    private synchronized PreparedStatement createQuery(Tratamento tratamento, PreparedStatement statement) {
        return null;
    }
}
