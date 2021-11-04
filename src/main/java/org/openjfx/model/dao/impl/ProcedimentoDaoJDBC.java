package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.model.dao.ProcedimentoDao;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.service.ColaboradorService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedimentoDaoJDBC implements ProcedimentoDao {
    private final Connection connection;

    public ProcedimentoDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }

    @Override
    public synchronized void insert(Procedimento procedimento) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO Procedimento "
                            + "(descricao, valor, id_especialista) "
                            + "VALUES (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            createQuery(procedimento, statement);

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
    public void update(Procedimento procedimento) {
        System.out.println("Chegou aqui");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE Procedimento"
                            + " SET descricao = ?,valor = ?"
                            + "WHERE id_Procedimento = ?");

            createQuery(procedimento, statement);
            System.out.println(statement);
            try {
                statement.setInt(3, procedimento.getIdProcedimento());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println(procedimento.getIdProcedimento());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer idProcedimento) {
        PreparedStatement statement = null;
        System.out.println(idProcedimento);
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM"
                            + " procedimento "
                            + "WHERE id_Procedimento = ?;");

            statement.setInt(1, idProcedimento);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Procedimento findById(Integer idProcedimento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM Procedimento "
                            + "WHERE id_Procedimento = ?");

            statement.setInt(1, idProcedimento);
            rs = statement.executeQuery();
            if (rs.next()) {
                Procedimento procedimento = instantiateProcedimento(rs);
                return procedimento;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    //    private Department instantiateDepartment(ResultSet rs) throws SQLException {
//        Department dep = new Department();
//        dep.setId(rs.getInt("DepartmentId"));
//        dep.setName(rs.getString("DepName"));
//        return dep;
//    }

    @Override
    public List<Procedimento> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM Procedimento ORDER BY descricao");
            rs = statement.executeQuery();
            List<Procedimento> list = new ArrayList<>();
            while (rs.next()) {
                Procedimento Procedimento = instantiateProcedimento(rs);
                list.add(Procedimento);
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
    public List<Procedimento> findAllAtivos() {
        return null;
    }

    @Override
    public List<Procedimento> findByEspecilistaId(Integer idEspecialista) {

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * "
                            + "FROM Procedimento "
                            + "WHERE id_especialista = ?");

            statement.setInt(1, idEspecialista);
            rs = statement.executeQuery();
            List<Procedimento> list = new ArrayList<>();
            while (rs.next()) {
                Procedimento Procedimento = instantiateProcedimento(rs);
                list.add(Procedimento);
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
    public List<Procedimento> findByDescricao(String descricao) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM procedimento " +
                            "WHERE descricao like ? " +
                            "ORDER BY descricao");
            statement.setString(1, "%" + descricao + "%");
            rs = statement.executeQuery();
            List<Procedimento> list = new ArrayList<>();
            while (rs.next()) {
                Procedimento procedimento = instantiateProcedimento(rs);
                list.add(procedimento);
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
    public List<Procedimento> findByDescricaoAndId(String descricao, Integer id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM procedimento " +
                            "WHERE descricao like ?  " +
                            "and id_especialista = ? " +
                            "ORDER BY descricao");
            statement.setString(1, "%" + descricao + "%");
            statement.setInt(2, id);
            rs = statement.executeQuery();
            List<Procedimento> list = new ArrayList<>();
            while (rs.next()) {
                Procedimento procedimento = instantiateProcedimento(rs);
                list.add(procedimento);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private synchronized PreparedStatement createQuery(Procedimento procedimento, PreparedStatement statement) {
        try {
            statement.setString(1, procedimento.getDescricao());
            statement.setDouble(2, procedimento.getValor());
            statement.setInt(3, procedimento.getColaborador().getIdColaborador());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }
    }

    private Procedimento instantiateProcedimento(ResultSet rs) throws SQLException {
        Procedimento Procedimento = new Procedimento();
        ColaboradorService colaboradorService = new ColaboradorService();
        Procedimento.setDescricao(rs.getString("descricao"));
        Procedimento.setIdProcedimento(rs.getInt("id_Procedimento"));
        Procedimento.setValor(rs.getDouble("valor"));
        Procedimento.setColaborador(colaboradorService.findById(rs.getInt("id_especialista")));
        return Procedimento;
    }


}
