package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.ProcedimentoMapper;
import org.openjfx.mapper.TratamentoMapper;
import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.ItensTratamento;
import org.openjfx.model.service.ProcedimentoService;
import org.openjfx.model.service.TratamentoService;

import java.sql.*;
import java.util.ArrayList;
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
                    "INSERT INTO itens_tratamento "
                            + "(tratamento,\"nrItem\", procedimento, quantidade, valor) "
                            + "VALUES (?,?,?,?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setInt(1, itensTratamento.getTratamento().getIdTratamento());
                statement.setInt(2, itensTratamento.getNrItem());
                statement.setInt(3, itensTratamento.getProcedimento().getIdProcedimento());
                statement.setInt(4, itensTratamento.getQuantidade());
                statement.setDouble(5, itensTratamento.getProcedimento().getValor());

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
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE itens_tratamento "
                            + "SET data_execucao = ?"
                            + "WHERE procedimento = ? "
                            + "and tratamento = ? " +
                            "and \"nrItem\" = ? ");
            try {
                if (itensTratamentoDao.getDataExecucao() != null)
                    statement.setDate(1, new Date(itensTratamentoDao.getDataExecucao().getTime()));
                else
                    statement.setNull(1, Types.DATE);
                statement.setInt(2, itensTratamentoDao.getProcedimento().getIdProcedimento());
                statement.setInt(3, itensTratamentoDao.getTratamento().getIdTratamento());
                statement.setInt(4, itensTratamentoDao.getNrItem());
            } catch (SQLException e) {
                System.out.println(statement);
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
    public List<ItensTratamento> findAll() {
        return null;
    }

    @Override
    public void deleteById(Integer nrItem) {

    }

    @Override
    public List<ItensTratamento> findByDescricao(String descricao) {
        return null;
    }

    @Override
    public List<ItensTratamento> findByTratamentoId(Integer idTratamento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT tratamento, procedimento, sum(valor) as valor, sum(quantidade) as quantidade " +
                            "FROM itens_tratamento " +
                            "where tratamento = ? " +
                            " group by tratamento, procedimento");

            statement.setInt(1, idTratamento);
            rs = statement.executeQuery();

            List<ItensTratamento> list = new ArrayList<>();

            while (rs.next()) {

                ItensTratamento itensTratamento = instantiateItensTratamento(rs);
                list.add(itensTratamento);
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
    public List<ItensTratamento> findByPaciente(Integer idPaciente) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM itens_tratamento, tratamento " +
                            "where public.tratamento.paciente = ? and " +
                            "itens_tratamento.tratamento = public.tratamento.id_tratamento " +
                            "and public.tratamento.status = \'APROVADO\' ");

            statement.setInt(1, idPaciente);
            rs = statement.executeQuery();

            List<ItensTratamento> list = new ArrayList<>();

            while (rs.next()) {

                ItensTratamento itensTratamento = instantiateItensTratamento(rs);
                list.add(itensTratamento);
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
    public ItensTratamento findByTratamentoIdAndProcedimentoId(Integer idTratamento, Integer idProcedimento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM itens_tratamento " +
                            "where tratamento = ? " +
                            "AND procedimento =?");

            statement.setInt(1, idTratamento);
            statement.setInt(2, idProcedimento);
            rs = statement.executeQuery();

            List<ItensTratamento> list = new ArrayList<>();

            while (rs.next()) {
                ItensTratamento itensTratamento = instantiateItensTratamento(rs);
                list.add(itensTratamento);
            }
            if (!list.isEmpty()) {
                return list.get(list.size() - 1);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    private ItensTratamento instantiateItensTratamento(ResultSet rs) {
        TratamentoService tratamentoService = new TratamentoService();
        ProcedimentoService procedimentoService = new ProcedimentoService();
        TratamentoMapper tratamentoMapper = new TratamentoMapper();
        ProcedimentoMapper procedimentoMapper = new ProcedimentoMapper();
        ItensTratamento itensTratamento = new ItensTratamento();
        try {
            itensTratamento.setTratamento(tratamentoMapper.toEntity(tratamentoService.findById(rs.getInt("tratamento"))));
            itensTratamento.setProcedimento(procedimentoMapper.toEntity(procedimentoService.findById(rs.getInt("procedimento"))));
            itensTratamento.setQuantidade(rs.getInt("quantidade"));
            itensTratamento.setValor(rs.getDouble("valor"));
            try {
                itensTratamento.setDataExecucao(rs.getDate("data_execucao"));
            } catch (Exception ex){
                System.out.println("não se aplica");
            }

            try {
                itensTratamento.setNrItem(rs.getInt("nrItem"));
            } catch (SQLException e) {
                System.out.println("não veio nrItem");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itensTratamento;
    }
}
