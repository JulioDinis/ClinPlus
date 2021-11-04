package org.openjfx.model.dao.impl;

import org.openjfx.db.DB;
import org.openjfx.db.DbException;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dao.AgendaDao;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.ItensTratamento;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.PacienteService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AgendaDaoJDBC implements AgendaDao {

    private Connection connection;

    public AgendaDaoJDBC(Connection newConnetion) {
        this.connection = newConnetion;
    }
    @Override
    public void insert(Agenda agenda) {
        System.out.println("Salvou?" +agenda);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO agenda "
                            + "(data,paciente,especialista,horario) "
                            + "VALUES (?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            createQuery(agenda, statement);
            Integer linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int idAgenda = rs.getInt(1);
                    agenda.setId(idAgenda);
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
    private synchronized PreparedStatement createQuery(Agenda agenda, PreparedStatement statement) {

        System.out.println("Agenda Recebida"+ agenda.getHorario());

        try {
            statement.setDate(1, new Date(agenda.getData().getTime()));
            statement.setInt(2, agenda.getPaciente().getIdPaciente());
            statement.setInt(3, agenda.getEspecialista().getIdColaborador());
            statement.setTime(4, agenda.getHorario());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e.getMessage());
        }


    }

    @Override
    public void update(Agenda agenda) {
//TODO implementar o reagendar (Agenda Antiga, Nova DATA_HORA)

    }

    @Override
    public void deleteById(Integer idAgenda) {
//TODO implementar o delete by ID
    }

    @Override
    public Agenda findById(Integer idAgenda) {
        //TODO implementar o findBYid
        return new Agenda();
    }

    @Override
    public List<Agenda> findByDate(java.sql.Date data) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM agenda " +
                            "where data = ? " +
                            "order by horario");
            statement.setDate(1, data);
            rs = statement.executeQuery();
            List<Agenda> list = new ArrayList<>();
            while (rs.next()) {
                Agenda itenAgenda = instantiateAgenda(rs);
                list.add(itenAgenda);
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
    public List<Agenda> findByDateAndEspecialista(java.sql.Date data, Colaborador especialista) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM agenda " +
                            "where data = ? " +
                            "and especialista = ? " +
                            "order by horario");
            statement.setDate(1, data);
            statement.setInt(2, especialista.getIdColaborador());
            rs = statement.executeQuery();
            List<Agenda> list = new ArrayList<>();
            while (rs.next()) {
                Agenda itenAgenda = instantiateAgenda(rs);
                list.add(itenAgenda);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }


    private Agenda instantiateAgenda(ResultSet rs) {
        ColaboradorService colaboradorService = new ColaboradorService();
        PacienteService pacienteService = new PacienteService();
        Agenda agenda = new Agenda();
        try {
            agenda.setId(rs.getInt("id_agenda"));
            agenda.setData(rs.getDate("data"));
            agenda.setHorario(rs.getTime("horario"));
            agenda.setEspecialista(colaboradorService.findById(rs.getInt("especialista")));
            agenda.setPaciente(pacienteService.findById(rs.getInt("paciente")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agenda;
    }

    @Override
    public List<Agenda> findAll() {
        return null;
    }
}
