package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.PacienteDao;
import org.openjfx.model.entities.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDaoJDBC implements PacienteDao {
    private final Connection connection;

    public PacienteDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public synchronized void insert(Paciente paciente) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO paciente "
                            + "(nome,cpf,rg,data_nascimento,sexo,email,logradouro,"
                            + "cidade,bairro,cep,uf,telefone,whats_app) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            createQuery(paciente, statement);

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int idPessoa = rs.getInt(1);
                    paciente.setIdPessoa(idPessoa);
                    paciente.setIdPaciente(idPessoa);
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
    public void update(Paciente paciente) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE paciente"
                            + " SET nome = ?,cpf = ?,rg = ?,data_nascimento = ?,"
                            + "sexo = ?,email = ?,logradouro = ?,cidade = ?,bairro = ?,"
                            + "cep = ?,uf = ?,telefone = ?, whats_app = ? "
                            + "WHERE id_paciente = ?");

            createQuery(paciente, statement);
            System.out.println(statement);
            try {
                statement.setInt(14, paciente.getIdPaciente());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println(paciente.getIdPaciente());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer idPaciente) {
        PreparedStatement statement = null;
        System.out.println(idPaciente);
        try {
            statement = connection.prepareStatement(
                    "UPDATE paciente"
                            + " SET ativo = ?"
                            + "WHERE id_paciente = ?;");

            statement.setBoolean(1, false);
            statement.setInt(2, idPaciente);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Paciente findById(Integer idPaciente) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM paciente "
                            + "WHERE id_paciente = ?");

            statement.setInt(1, idPaciente);
            rs = statement.executeQuery();
            if (rs.next()) {
                Paciente paciente = instantiatePaciente(rs);
                return paciente;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Paciente> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM paciente ORDER BY nome");

            rs = statement.executeQuery();

            List<Paciente> list = new ArrayList<>();

            while (rs.next()) {

                Paciente paciente = instantiatePaciente(rs);
                list.add(paciente);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Paciente> findAllAtivos() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM paciente " +
                            "WHERE ativo = ?" +
                            "ORDER BY nome");

            statement.setBoolean(1, true);
            rs = statement.executeQuery();

            List<Paciente> list = new ArrayList<>();

            while (rs.next()) {

                Paciente paciente = instantiatePaciente(rs);
                list.add(paciente);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Paciente> findByName(String name) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM paciente " +
                            "WHERE nome like ? " +
                            "and ativo = true " +
                            "ORDER BY nome");
            statement.setString(1, "%"+name+"%");
            rs = statement.executeQuery();
            List<Paciente> list = new ArrayList<>();
            while (rs.next()) {
                Paciente paciente = instantiatePaciente(rs);
                list.add(paciente);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private synchronized PreparedStatement createQuery(Paciente paciente, PreparedStatement statement) {
        try {
            statement.setString(1, paciente.getNome());
            statement.setString(2, paciente.getCpf());
            statement.setString(3, paciente.getRg());
            statement.setDate(4, new Date(paciente.getDataNascimento().getTime()));
            statement.setString(5, paciente.getSexo());
            statement.setString(6, paciente.getEmail());
            statement.setString(7, paciente.getLogradouro());
            statement.setString(8, paciente.getCidade());
            statement.setString(9, paciente.getBairro());
            statement.setString(10, paciente.getCep());
            statement.setString(11, paciente.getUf());
            statement.setString(12, paciente.getTelefone());
            statement.setString(13, paciente.getWhatsApp());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    private Paciente instantiatePaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();

        paciente.setIdPessoa(rs.getInt("id_pessoa"));
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNome(rs.getString("nome"));
        paciente.setLogradouro(rs.getString("logradouro"));
        paciente.setBairro(rs.getString("bairro"));
        paciente.setCidade(rs.getString("cidade"));
        paciente.setCep(rs.getString("cep"));
        paciente.setUf(rs.getString("uf"));
        paciente.setSexo(rs.getString("sexo"));
        paciente.setRg(rs.getString("rg"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setWhatsApp(rs.getString("whats_app"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDataNascimento(new java.util.Date(rs.getTimestamp("data_nascimento").getTime()));

        return paciente;
    }


}
