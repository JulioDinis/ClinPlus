package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.mapper.CaixaMensalMapper;
import org.openjfx.model.dao.AporteDao;
import org.openjfx.model.entities.Aporte;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.FinanceiroService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AporteDaoJDBC implements AporteDao {

    private final Connection connection;

    public AporteDaoJDBC(Connection newConnection) {
        this.connection = newConnection;
    }

    @Override
    public void insert(Aporte aporte) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO aporte "
                            + "(valor, descricao, data, id_colaborador, mes, ano) "
                            + "VALUES (?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            createQuery(aporte, statement);
            int linhasAfetadas = statement.executeUpdate();
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

    private void createQuery(Aporte aporte, PreparedStatement statement) {
        try {
            statement.setDouble(1, aporte.getValor());
            statement.setString(2, aporte.getDescricao());
            statement.setDate(3, aporte.getData());
            statement.setInt(4, aporte.getColaborador().getIdColaborador());
            statement.setInt(5, aporte.getCaixaMensal().getMes());
            statement.setInt(6, aporte.getCaixaMensal().getAno());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public void update(Aporte aporte) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE aporte"
                            + " SET valor= ?, " +
                            "descricao= ?, " +
                            "data= ?, " +
                            "id_colaborador= ?, " +
                            "mes= ?, " +
                            "ano= ?  "
                            + "WHERE id_aporte = ?");

            try {
                statement.setDouble(1, aporte.getValor());
                statement.setString(2, aporte.getDescricao());
                statement.setDate(3, aporte.getData());
                statement.setInt(4, aporte.getColaborador().getIdColaborador());
                statement.setInt(5, aporte.getCaixaMensal().getMes());
                statement.setInt(6, aporte.getCaixaMensal().getAno());
                statement.setInt(7, aporte.getId());

            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer idAporte) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM aporte "
                            + "WHERE id_aporte = ?;");
            statement.setInt(1, idAporte);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Aporte findById(Integer idAporte) {
        PreparedStatement statement = null;
        Aporte aporte = new Aporte();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM aporte " +
                            "WHERE id_aporte =? " +
                            "ORDER BY data");
            statement.setInt(1, idAporte);
            rs = statement.executeQuery();
            if (rs.next()) {
                aporte = instantiateAporte(rs);
            }
            return aporte;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Aporte> findAll() {
        PreparedStatement statement = null;
        List<Aporte> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM aporte ORDER BY data");
            rs = statement.executeQuery();
            System.out.println(list);
            while (rs.next()) {
                Aporte aporte = instantiateAporte(rs);
                list.add(aporte);
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
    public List<Aporte> findByCaixaMensal(CaixaMensal caixaMensal) {
        PreparedStatement statement = null;
        List<Aporte> list = new ArrayList<>();

        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM aporte " +
                            "WHERE mes =? " +
                            "and ano =? " +
                            "ORDER BY data");
            statement.setInt(1, caixaMensal.getMes());
            statement.setInt(2, caixaMensal.getAno());
            rs = statement.executeQuery();
            while (rs.next()) {
                Aporte aporte = instantiateAporte(rs);
                list.add(aporte);

            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private Aporte instantiateAporte(ResultSet rs) {
        FinanceiroService financeiroService = new FinanceiroService();
        ColaboradorService colaboradorService = new ColaboradorService();
        CaixaMensalMapper mapper = new CaixaMensalMapper();
        Aporte novoAporte = new Aporte();
        try {
            novoAporte = new Aporte(
                    rs.getInt("id_aporte"),
                    rs.getDouble("valor"),
                    rs.getString("descricao"),
                    rs.getDate("data"),
                    colaboradorService.findById(rs.getInt("id_colaborador")),
                    mapper.toEntity(financeiroService.buscarCaixaById(rs.getInt(6), rs.getInt(7)))
            );
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return novoAporte;
    }
}
