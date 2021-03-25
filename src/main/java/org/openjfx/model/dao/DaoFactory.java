package org.openjfx.model.dao;

import org.openjfx.db.DB;
import org.openjfx.model.dao.impl.FuncionarioDaoJDBC;
import org.openjfx.model.dao.impl.PacienteDaoJDBC;

public class DaoFactory {
    public static PacienteDao createPacienteDao(String creator) {
        return new PacienteDaoJDBC(DB.getConnection("Dao Factory - Create Paciente DAO  | Pedido By: " + creator));
    }

    public static FuncionarioDao createFuncionarioDao(String creator) {
        return new FuncionarioDaoJDBC(DB.getConnection("Dao Factory - Create Paciente DAO  | Pedido By: " + creator)) {
        };
    }
}
