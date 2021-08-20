package org.openjfx.model.dao;

import org.openjfx.db.DB;
import org.openjfx.model.dao.impl.*;

public class DaoFactory {
    public static PacienteDao createPacienteDao(String creator) {
        return new PacienteDaoJDBC(DB.getConnection("Dao Factory - Create Paciente DAO  | Pedido By: " + creator));
    }

    public static ColaboradorDao createFuncionarioDao(String creator) {
        return new ColaboradorDaoJDBC(DB.getConnection("Dao Factory - Create Colaborador DAO  | Pedido By: " + creator)) {
        };
    }

    public static AgendaDao createAgendaDao(String creator) {
        return new AgendaDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }

    public static AporteDao createAporteDao(String creator) {
        return new AporteDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }

    public static CaixaMensalDao createCaixaMensalDao(String creator) {
        return new CaixaMensalDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }

    public static ItensTratamentoDao createItensTratamentoDao(String creator) {
        return new ItensTratamentoDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }

    public static ProcedimentoDao createProcedimentoDao(String creator) {
        return new ProcedimentoDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }

    public static TratamentoDao createTratamentoDao(String creator) {
        return new TratamentoDaoJDBC(DB.getConnection("Dao Factory - Create Tratamento DAO  | Pedido By: " + creator)) {
        };
    }

    public static AtendenteDao createAtendenteDao(String creator) {
        return new AtendenteDaoJDBC(DB.getConnection("Dao Factory - Create Tratamento DAO  | Pedido By: " + creator)) {
        };
    }
}
