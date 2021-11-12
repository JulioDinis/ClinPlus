package org.openjfx.model.service;

import org.openjfx.model.dao.CaixaMensalDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.entities.CaixaMensal;

public class FinanceiroService {

    private CaixaMensalDao dao = DaoFactory.createCaixaMensalDao("Financeiro Service");

    public void abrirCaixa(CaixaMensal caixaMensal) {
        dao.insert(caixaMensal);
    }

    public CaixaMensal fecharCaixa(CaixaMensal caixaMensal) {
        caixaMensal.setStatus("Fechado");
        dao.update(caixaMensal);
        return caixaMensal;
    }

    public CaixaMensal buscarCaixaById(Integer mes, Integer ano){
        return  dao.findById(mes, ano);

    }
}
