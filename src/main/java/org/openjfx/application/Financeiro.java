package org.openjfx.application;

import org.openjfx.model.dao.CaixaMensalDao;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.FinanceiroService;

public class Financeiro {

    public static final FinanceiroService service = new FinanceiroService();

    public static void main(String[] args) {
        CaixaMensal cx = new CaixaMensal(10, 2021, 20D);
     //   System.out.println(cx);
     //   service.abrirCaixa(cx);
        cx.setSaldoFinal(32D);
        cx = service.fecharCaixa(cx);
        System.out.println(cx);
    }
}
