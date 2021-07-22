package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.mapper.TratamentoMapper;
import org.openjfx.model.dao.ItensTratamentoDao;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.Colaborador;
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
        System.out.println("Chegou aqui");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE itens_orcamento"
                            + " SET quantidade = ?"
                            + "WHERE procedimento = ? "
                            + "and orcamento = ?");
            try {
                statement.setInt(1, itensTratamentoDao.getQuantidade());
                statement.setInt(2, itensTratamentoDao.getProcedimento().getIdProcedimento());
                statement.setInt(3, itensTratamentoDao.getTratamento().getIdTratamento());
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
    public List<ItensTratamentoDto> findByDescricao(String descricao) {
        return null;
    }

    @Override
    public List<ItensTratamentoDto> findByTratamentoId(Integer idTratamento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM itens_orcamento " +
                            "where orcamento = ? ");

            statement.setInt(1, idTratamento);
            rs = statement.executeQuery();

            List<ItensTratamentoDto> list = new ArrayList<>();

            while (rs.next()) {

                ItensTratamentoDto itensTratamentoDto = instantiateItensTratamentoDto(rs);
                list.add(itensTratamentoDto);
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
                    "SELECT * FROM itens_orcamento " +
                            "where orcamento = ? " +
                            "AND procedimento =?");

            statement.setInt(1, idTratamento);
            statement.setInt(2, idProcedimento);
            rs = statement.executeQuery();

            List<ItensTratamentoDto> list = new ArrayList<>();

            if (rs.next()) {
                ItensTratamento itensTratamento = instantiateItensTratamento(rs);
                return itensTratamento;
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
        ItensTratamento itensTratamento = new ItensTratamento();
        try {
            itensTratamento.setTratamento(tratamentoMapper.toEntity(tratamentoService.findById(rs.getInt("orcamento"))));
            itensTratamento.setProcedimento(procedimentoService.findById(rs.getInt("procedimento")));
            itensTratamento.setQuantidade(rs.getInt("quantidade"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itensTratamento;
    }

    private ItensTratamentoDto instantiateItensTratamentoDto(ResultSet rs) {
        ItensTratamentoDto itensTratamentoDto = new ItensTratamentoDto();
        TratamentoService tratamentoService = new TratamentoService();
        ProcedimentoService procedimentoService = new ProcedimentoService();
        TratamentoMapper tratamentoMapper = new TratamentoMapper();

        try {
            itensTratamentoDto.setTratamento(tratamentoMapper.toEntity(tratamentoService.findById(rs.getInt("orcamento"))));
            itensTratamentoDto.setProcedimento(procedimentoService.findById(rs.getInt("procedimento")));
            itensTratamentoDto.setQuantidade(rs.getInt("quantidade"));
            itensTratamentoDto.setDescricao(itensTratamentoDto.getProcedimento().getDescricao());
            itensTratamentoDto.setValor(itensTratamentoDto.getProcedimento().getValor());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return itensTratamentoDto;
    }
}
