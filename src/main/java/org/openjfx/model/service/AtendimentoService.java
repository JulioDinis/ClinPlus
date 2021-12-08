package org.openjfx.model.service;

import org.openjfx.model.dao.AtendimentoDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.entities.Atendimento;

public class AtendimentoService {

    AtendimentoDao dao = DaoFactory.createAtendimentoDao("Atendimento Service");
    public void salvar(Atendimento atendimento){
        dao.insert(atendimento);
    }

}
