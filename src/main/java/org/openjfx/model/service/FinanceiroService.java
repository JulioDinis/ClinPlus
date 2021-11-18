package org.openjfx.model.service;

import org.openjfx.mapper.CaixaMensalMapper;
import org.openjfx.mapper.ColaboradorMapper;
import org.openjfx.model.dao.CaixaMensalDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dto.CaixaMensalDTO;
import org.openjfx.model.entities.CaixaMensal;

import java.util.List;

public class FinanceiroService {

    private CaixaMensalDao dao = DaoFactory.createCaixaMensalDao("Financeiro Service");
    private  final CaixaMensalMapper mapper = new CaixaMensalMapper();

    public void abrirCaixa(CaixaMensal caixaMensal) {
        dao.insert(caixaMensal);
    }

    public CaixaMensal fecharCaixa(CaixaMensal caixaMensal) {
        caixaMensal.setStatus("Fechado");
        dao.update(caixaMensal);
        return caixaMensal;
    }
    public CaixaMensalDTO buscarCaixaById(Integer mes, Integer ano){
        return  mapper.toDto(dao.findById(mes, ano));
    }
    public List<CaixaMensal> buscaCaixaAberto(){
        return  dao.findAllAberto();
    }

    public Double buscaSaldoAtual(CaixaMensalDTO caixaMensalDTO) {
        // TODO implementar (saldo atual = saldo inicial + (aportes - contas))
        return 0D;
    }
}
