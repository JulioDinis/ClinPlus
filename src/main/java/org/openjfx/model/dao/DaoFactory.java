package org.openjfx.model.dao;

import org.openjfx.db.DB;
import org.openjfx.model.dao.impl.FuncionarioDaoJDBC;
import org.openjfx.model.dao.impl.PacienteDaoJDBC;
import org.openjfx.model.dao.impl.ProcedimentoDaoJDBC;

public class DaoFactory {
    public static PacienteDao createPacienteDao(String creator) {
        return new PacienteDaoJDBC(DB.getConnection("Dao Factory - Create Paciente DAO  | Pedido By: " + creator));
    }

    public static FuncionarioDao createFuncionarioDao(String creator) {
        return new FuncionarioDaoJDBC(DB.getConnection("Dao Factory - Create Funcionario DAO  | Pedido By: " + creator)) {
        };
    }
    public static ProcedimentoDao createProcedimentoDao(String creator) {
        return new ProcedimentoDaoJDBC(DB.getConnection("Dao Factory - Create Procedimento DAO  | Pedido By: " + creator)) {
        };
    }
}
