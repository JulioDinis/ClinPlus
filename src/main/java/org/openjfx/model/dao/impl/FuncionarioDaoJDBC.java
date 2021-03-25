package org.openjfx.model.dao.impl;

import com.google.common.hash.Hashing;
import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.FuncionarioDao;
import org.openjfx.model.entities.Funcionario;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDaoJDBC implements FuncionarioDao {
    private Connection connection;

    public FuncionarioDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public synchronized void insert(Funcionario funcionario) {
        System.out.println("Yeeppp");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO funcionario "
                            + "(nome,cpf,rg,data_nascimento,sexo,email,logradouro,"
                            + "cidade,bairro,cep,uf,telefone,funcao, especialidade, senha, salario) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            createQuery(funcionario, statement);

            Integer linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int idPessoa = rs.getInt(1);
                    funcionario.setIdPessoa(idPessoa);
                    funcionario.setIdFuncionario(idPessoa);
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

    private synchronized PreparedStatement createQuery(Funcionario funcionario, PreparedStatement statement) {

        try {
            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getCpf());
            statement.setString(3, funcionario.getRg());
            statement.setDate(4, new Date(funcionario.getDataNascimento().getTime()));
            statement.setString(5, funcionario.getSexo());
            statement.setString(6, funcionario.getEmail());
            statement.setString(7, funcionario.getLogradouro());
            statement.setString(8, funcionario.getCidade());
            statement.setString(9, funcionario.getBairro());
            statement.setString(10, funcionario.getCep());
            statement.setString(11, funcionario.getUf());
            statement.setString(12, funcionario.getTelefone());
            statement.setString(13, funcionario.getFuncao());
            statement.setString(14, funcionario.getEspecialidade());
            statement.setString(15, funcionario.getSenha());
            statement.setDouble(16, funcionario.getSalario());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Funcionario funcionario) {
        System.out.println("Chegou aqui");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE funcionario"
                            + " SET nome = ?,cpf = ?,rg = ?,data_nascimento = ?,"
                            + "sexo = ?,email = ?,logradouro = ?,cidade = ?,bairro = ?,"
                            + "cep = ?,uf = ?,telefone = ?, funcao = ?, especialidade = ?, senha = ?, salario = ? "
                            + "WHERE id_funcionario = ?");

            createQuery(funcionario, statement);

            try {
                statement.setInt(17, funcionario.getIdFuncionario());
            } catch (SQLException e) {
                System.out.println(statement);
                e.printStackTrace();
            }

            System.out.println(funcionario.getIdFuncionario());
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
                    "UPDATE funcionario"
                            + " SET ativo = ?, data_desligamento = ?"
                            + "WHERE id_funcionario = ?");

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
    public Funcionario findById(Integer idFuncionario) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM funcionario "
                            + "WHERE id_funcionario = ?");

            statement.setInt(1, idFuncionario);
            rs = statement.executeQuery();
            if (rs.next()) {
                Funcionario funcionario = instantiateFuncionario(rs);
                return funcionario;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private Funcionario instantiateFuncionario(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();

        funcionario.setIdPessoa(rs.getInt("id_pessoa"));
        funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setLogradouro(rs.getString("logradouro"));
        funcionario.setBairro(rs.getString("bairro"));
        funcionario.setCidade(rs.getString("cidade"));
        funcionario.setCep(rs.getString("cep"));
        funcionario.setUf(rs.getString("uf"));
        funcionario.setSexo(rs.getString("sexo"));
        funcionario.setRg(rs.getString("rg"));
        funcionario.setCpf(rs.getString("cpf"));
        funcionario.setTelefone(rs.getString("telefone"));
        funcionario.setFuncao(rs.getString("funcao"));
        funcionario.setEmail(rs.getString("email"));
        funcionario.setEspecialidade(rs.getString("especialidade"));
        funcionario.setSalario(rs.getDouble("salario"));
        funcionario.setDataContrato(rs.getDate("data_contrato"));
        funcionario.setDataDesligamento(rs.getDate("data_desligamento"));
        funcionario.setAtivo(rs.getBoolean("ativo"));
        funcionario.setDataNascimento(new java.util.Date(rs.getTimestamp("data_nascimento").getTime()));

        return funcionario;
    }

    //    private Department instantiateDepartment(ResultSet rs) throws SQLException {
//        Department dep = new Department();
//        dep.setId(rs.getInt("DepartmentId"));
//        dep.setName(rs.getString("DepName"));
//        return dep;
//    }

    @Override
    public List<Funcionario> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM funcionario ORDER BY nome");

            rs = statement.executeQuery();

            List<Funcionario> list = new ArrayList<>();

            while (rs.next()) {

                Funcionario funcionario = instantiateFuncionario(rs);
                list.add(funcionario);
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
    public List<Funcionario> findAllAtivos() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM funcionario " +
                            "WHERE ativo = ?" +
                            "ORDER BY nome");

            statement.setBoolean(1, true);
            rs = statement.executeQuery();

            List<Funcionario> list = new ArrayList<>();

            while (rs.next()) {

                Funcionario funcionario = instantiateFuncionario(rs);
                list.add(funcionario);
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
    public Funcionario logar(Integer idFuncionario, String senha) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM funcionario " +
                            "WHERE id_funcionario = ?" +
                            "AND senha= ?" +
                            "AND ativo= ?" +
                            "ORDER BY nome");

            statement.setInt(1, idFuncionario);
            statement.setString(2, senha);
            statement.setBoolean(3, true);
            rs = statement.executeQuery();

            List<Funcionario> list = new ArrayList<>();
            System.out.println(list);
            while (rs.next()) {

                Funcionario funcionario = instantiateFuncionario(rs);
                list.add(funcionario);
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

}
