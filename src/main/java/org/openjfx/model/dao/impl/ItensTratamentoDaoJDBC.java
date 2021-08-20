package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
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
                            + "(tratamento, procedimento, quantidade, valor) "
                            + "VALUES (?,?,?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            try {
                statement.setInt(1, itensTratamento.getTratamento().getIdTratamento());
                statement.setInt(2, itensTratamento.getProcedimento().getIdProcedimento());
                statement.setInt(3, itensTratamento.getQuantidade());
                statement.setDouble(4, itensTratamento.getProcedimento().getValor());

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
                    "UPDATE itens_tratamento "
                            + "SET quantidade = ?"
                            + "WHERE procedimento = ? "
                            + "and tratamento = ?");
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
    public List<ItensTratamentoDTO> findByDescricao(String descricao) {
        return null;
    }

    @Override
    public List<ItensTratamentoDTO> findByTratamentoId(Integer idTratamento) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM itens_tratamento " +
                            "where tratamento = ? ");

            statement.setInt(1, idTratamento);
            rs = statement.executeQuery();

            List<ItensTratamentoDTO> list = new ArrayList<>();

            while (rs.next()) {

                ItensTratamentoDTO itensTratamentoDto = instantiateItensTratamentoDto(rs);
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
                    "SELECT * FROM itens_tratamento " +
                            "where tratamento = ? " +
                            "AND procedimento =?");

            statement.setInt(1, idTratamento);
            statement.setInt(2, idProcedimento);
            rs = statement.executeQuery();

            List<ItensTratamentoDTO> list = new ArrayList<>();

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
        ProcedimentoMapper procedimentoMapper = new ProcedimentoMapper();
        ItensTratamento itensTratamento = new ItensTratamento();
        try {
            itensTratamento.setTratamento(tratamentoMapper.toEntity(tratamentoService.findById(rs.getInt("tratamento"))));
            itensTratamento.setProcedimento(procedimentoMapper.toEntity(procedimentoService.findById(rs.getInt("procedimento"))));
            itensTratamento.setQuantidade(rs.getInt("quantidade"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itensTratamento;
    }

    private ItensTratamentoDTO instantiateItensTratamentoDto(ResultSet rs) {
        ItensTratamentoDTO itensTratamentoDto = new ItensTratamentoDTO();
        TratamentoService tratamentoService = new TratamentoService();
        ProcedimentoService procedimentoService = new ProcedimentoService();
        TratamentoMapper tratamentoMapper = new TratamentoMapper();
        ProcedimentoMapper procedimentoMapper = new ProcedimentoMapper();

        try {
            itensTratamentoDto.setTratamento(tratamentoMapper.toEntity(tratamentoService.findById(rs.getInt("tratamento"))));
            itensTratamentoDto.setProcedimento(procedimentoMapper.toEntity(procedimentoService.findById(rs.getInt("procedimento"))));
            itensTratamentoDto.setQuantidade(rs.getInt("quantidade"));
            itensTratamentoDto.setDescricao(itensTratamentoDto.getProcedimento().getDescricao());
            itensTratamentoDto.setValor(itensTratamentoDto.getProcedimento().getValor());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return itensTratamentoDto;
    }
}
