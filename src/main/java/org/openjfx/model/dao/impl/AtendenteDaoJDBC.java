package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.AtendenteDao;
import org.openjfx.model.entities.Atendente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AtendenteDaoJDBC implements AtendenteDao {
    private Connection connection;

    public AtendenteDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public synchronized void insert(Atendente atendente) {
        System.out.println("Yeeppp");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO atendente "
                            + "(nome,cpf,rg,data_nascimento,sexo,email,logradouro,"
                            + "cidade,bairro,cep,uf,telefone,senha, salario, data_admissao) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            createQuery(atendente, statement);

            Integer linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int idPessoa = rs.getInt(1);
                    atendente.setIdPessoa(idPessoa);
                    atendente.setIdAtendente(idPessoa);
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

    private synchronized PreparedStatement createQuery(Atendente atendente, PreparedStatement statement) {
        try {
            statement.setString(1, atendente.getNome());
            statement.setString(2, atendente.getCpf());
            statement.setString(3, atendente.getRg());
            statement.setDate(4, new Date(atendente.getDataNascimento().getTime()));
            statement.setString(5, atendente.getSexo());
            statement.setString(6, atendente.getEmail());
            statement.setString(7, atendente.getLogradouro());
            statement.setString(8, atendente.getCidade());
            statement.setString(9, atendente.getBairro());
            statement.setString(10, atendente.getCep());
            statement.setString(11, atendente.getUf());
            statement.setString(12, atendente.getTelefone());
            statement.setString(13, atendente.getSenha());
            statement.setDouble(14, atendente.getSalario());
            statement.setDate(15, new Date(atendente.getDataContrato().getTime()));
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Atendente atendente) {
        System.out.println("Chegou aqui");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE atendente"
                            + " SET nome = ?,cpf = ?,rg = ?,data_nascimento = ?,"
                            + "sexo = ?,email = ?,logradouro = ?,cidade = ?,bairro = ?,"
                            + "cep = ?,uf = ?,telefone = ?, senha = ?, salario = ? "
                            + "WHERE id_colaborador = ?");

            createQuery(atendente, statement);

            try {
                statement.setInt(15, atendente.getIdAtendente());
            } catch (SQLException e) {
                System.out.println(statement);
                e.printStackTrace();
            }

            System.out.println(atendente.getIdAtendente());
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
                    "UPDATE atendente"
                            + " SET ativo = ?, data_desligamento = ?"
                            + "WHERE id_colaborador= ?");

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
    public Atendente findById(Integer idFuncionario) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM atendente "
                            + "WHERE id_colaborador = ?");

            statement.setInt(1, idFuncionario);
            rs = statement.executeQuery();
            if (rs.next()) {
                Atendente atendente = instantiateAtendente(rs);
                return atendente;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private Atendente instantiateAtendente(ResultSet rs) throws SQLException {
        Atendente atendente = new Atendente();
        atendente.setIdPessoa(rs.getInt("id_pessoa"));
        atendente.setIdAtendente(rs.getInt("id_colaborador"));
        atendente.setNome(rs.getString("nome"));
        atendente.setLogradouro(rs.getString("logradouro"));
        atendente.setBairro(rs.getString("bairro"));
        atendente.setCidade(rs.getString("cidade"));
        atendente.setCep(rs.getString("cep"));
        atendente.setUf(rs.getString("uf"));
        atendente.setSexo(rs.getString("sexo"));
        atendente.setRg(rs.getString("rg"));
        atendente.setCpf(rs.getString("cpf"));
        atendente.setTelefone(rs.getString("telefone"));
        atendente.setEmail(rs.getString("email"));
        atendente.setSalario(rs.getDouble("salario"));
        atendente.setDataContrato(rs.getDate("data_admissao"));
        atendente.setDataDesligamento(rs.getDate("data_desligamento"));
        atendente.setAtivo(rs.getBoolean("ativo"));
        atendente.setDataNascimento(new java.util.Date(rs.getTimestamp("data_nascimento").getTime()));

        return atendente;
    }

    @Override
    public List<Atendente> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM atendente ORDER BY nome");

            rs = statement.executeQuery();

            List<Atendente> list = new ArrayList<>();

            System.out.println(list);
            while (rs.next()) {

                Atendente atendente = instantiateAtendente(rs);
                list.add(atendente);
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
    public List<Atendente> findAllAtivos() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM atendente " +
                            "WHERE ativo = ?" +
                            "ORDER BY nome");

            statement.setBoolean(1, true);
            rs = statement.executeQuery();

            List<Atendente> list = new ArrayList<>();
            System.out.println("Buscou -> ");
            System.out.println(list);
            while (rs.next()) {

                Atendente atendente = instantiateAtendente(rs);
                list.add(atendente);
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
    public Atendente logar(Integer idFuncionario, String senha) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM atendente " +
                            "WHERE id_colaborador = ?" +
                            "AND senha= ?" +
                            "AND ativo= ?" +
                            "ORDER BY nome");

            statement.setInt(1, idFuncionario);
            statement.setString(2, senha);
            statement.setBoolean(3, true);
            rs = statement.executeQuery();

            List<Atendente> list = new ArrayList<>();
            System.out.println(list);
            while (rs.next()) {

                Atendente atendente = instantiateAtendente(rs);
                list.add(atendente);
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
    public List<Atendente> findByName(String name) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM atendente " +
                            "WHERE nome like ? " +
                            "and ativo = true " +
                            "ORDER BY nome");
            statement.setString(1, "%" + name + "%");
            rs = statement.executeQuery();
            List<Atendente> list = new ArrayList<>();
            while (rs.next()) {
                Atendente atendente = instantiateAtendente(rs);
                list.add(atendente);
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
