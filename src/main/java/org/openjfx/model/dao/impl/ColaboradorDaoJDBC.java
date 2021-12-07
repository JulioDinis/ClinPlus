package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.ColaboradorDao;
import org.openjfx.model.entities.Colaborador;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorDaoJDBC implements ColaboradorDao {
    private Connection connection;

    public ColaboradorDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public synchronized void insert(Colaborador colaborador) {
        System.out.println("Yeeppp");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO especialista "
                            + "(nome,cpf,rg,data_nascimento,sexo,email,logradouro,"
                            + "cidade,bairro,cep,uf,telefone,especialidade, senha, conselho_regional) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            createQuery(colaborador, statement);

            Integer linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int idPessoa = rs.getInt(1);
                    colaborador.setIdPessoa(idPessoa);
                    colaborador.setIdColaborador(idPessoa);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhum registo afetado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);

        }

    }

    private synchronized PreparedStatement createQuery(Colaborador colaborador, PreparedStatement statement) {
        try {
            statement.setString(1, colaborador.getNome());
            statement.setString(2, colaborador.getCpf());
            statement.setString(3, colaborador.getRg());
            statement.setDate(4, new Date(colaborador.getDataNascimento().getTime()));
            statement.setString(5, colaborador.getSexo());
            statement.setString(6, colaborador.getEmail());
            statement.setString(7, colaborador.getLogradouro());
            statement.setString(8, colaborador.getCidade());
            statement.setString(9, colaborador.getBairro());
            statement.setString(10, colaborador.getCep());
            statement.setString(11, colaborador.getUf());
            statement.setString(12, colaborador.getTelefone());
            statement.setString(13, colaborador.getEspecialidade());
            statement.setString(14, colaborador.getSenha());
            statement.setString(15, colaborador.getConselhoRegional());

            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Colaborador colaborador) {
        System.out.println("Chegou aqui");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE especialista"
                            + " SET nome = ?,cpf = ?,rg = ?,data_nascimento = ?, "
                            + "sexo = ?,email = ?,logradouro = ?,cidade = ?,bairro = ?, "
                            + "cep = ?,uf = ?,telefone = ?, especialidade = ?, senha = ?, "
                            + " conselho_regional =? "
                            + "WHERE id_colaborador = ?");

            createQuery(colaborador, statement);

            try {
                statement.setInt(16, colaborador.getIdColaborador());
            } catch (SQLException e) {
                System.out.println(statement);
                e.printStackTrace();
            }

            System.out.println(colaborador.getIdColaborador());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer idFuncionario) {
        PreparedStatement statement = null;
        System.out.println(idFuncionario);
        try {
            statement = connection.prepareStatement(
                    "UPDATE especialista"
                            + " SET ativo = ?, data_desligamento = ?"
                            + "WHERE id_colaborador = ?");

            statement.setBoolean(1, false);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, idFuncionario);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Colaborador findById(Integer idFuncionario) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM especialista "
                            + "WHERE id_colaborador = ?");

            statement.setInt(1, idFuncionario);
            rs = statement.executeQuery();
            if (rs.next()) {
                Colaborador colaborador = instantiateFuncionario(rs);
                return colaborador;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private Colaborador instantiateFuncionario(ResultSet rs) throws SQLException {
        Colaborador colaborador = new Colaborador();
        colaborador.setIdPessoa(rs.getInt("id_pessoa"));
        colaborador.setIdColaborador(rs.getInt("id_colaborador"));
        colaborador.setNome(rs.getString("nome"));
        colaborador.setLogradouro(rs.getString("logradouro"));
        colaborador.setBairro(rs.getString("bairro"));
        colaborador.setCidade(rs.getString("cidade"));
        colaborador.setCep(rs.getString("cep"));
        colaborador.setUf(rs.getString("uf"));
        colaborador.setSexo(rs.getString("sexo"));
        colaborador.setRg(rs.getString("rg"));
        colaborador.setCpf(rs.getString("cpf"));
        colaborador.setTelefone(rs.getString("telefone"));
        colaborador.setConselhoRegional(rs.getString("conselho_regional"));
        colaborador.setEmail(rs.getString("email"));
        colaborador.setEspecialidade(rs.getString("especialidade"));
        colaborador.setDataContrato(rs.getDate("data_admissao"));
        colaborador.setDataDesligamento(rs.getDate("data_desligamento"));
        colaborador.setAtivo(rs.getBoolean("ativo"));
        colaborador.setDataNascimento(new java.util.Date(rs.getTimestamp("data_nascimento").getTime()));

        return colaborador;
    }

    @Override
    public List<Colaborador> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM especialista ORDER BY nome");

            rs = statement.executeQuery();

            List<Colaborador> list = new ArrayList<>();

            while (rs.next()) {

                Colaborador colaborador = instantiateFuncionario(rs);
                list.add(colaborador);
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
    public List<Colaborador> findAllAtivos() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM especialista " +
                            "WHERE ativo = ?" +
                            "ORDER BY nome");

            statement.setBoolean(1, true);
            rs = statement.executeQuery();

            List<Colaborador> list = new ArrayList<>();

            while (rs.next()) {

                Colaborador colaborador = instantiateFuncionario(rs);
                list.add(colaborador);
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
    public Colaborador logar(Integer idFuncionario, String senha) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM especialista " +
                            "WHERE id_colaborador = ?" +
                            "AND senha= ?" +
                            "AND ativo= ?" +
                            "ORDER BY nome");

            statement.setInt(1, idFuncionario);
            statement.setString(2, senha);
            statement.setBoolean(3, true);
            rs = statement.executeQuery();

            List<Colaborador> list = new ArrayList<>();
            System.out.println(list);
            while (rs.next()) {

                Colaborador colaborador = instantiateFuncionario(rs);
                list.add(colaborador);
            }
            if (list.isEmpty()) {
                return null;
            } else
                return list.get(0);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Colaborador> findByName(String name) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM especialista " +
                            "WHERE nome like ? " +
                            "and ativo = true " +
                            "ORDER BY nome");
            statement.setString(1, "%" + name + "%");
            rs = statement.executeQuery();
            List<Colaborador> list = new ArrayList<>();
            while (rs.next()) {
                Colaborador colaborador = instantiateFuncionario(rs);
                list.add(colaborador);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

}
