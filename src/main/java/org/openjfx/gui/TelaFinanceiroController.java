package org.openjfx.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.FinanceiroService;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaFinanceiroController implements Initializable {
    private FinanceiroService service;
    private CaixaMensal caixaAberto;

    @FXML
    private Label labelMesAno;
    @FXML
    private Label labelSaldoInicial;
    @FXML
    private Label saldoFinal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFinanceiroService(FinanceiroService financeiroService) {
        this.service = financeiroService;
    }

    public void updateTableView() {

    }

    public void setCaixaAberto(CaixaMensal caixaMensal) {
        this.caixaAberto = caixaMensal;
        labelMesAno.setText(caixaMensal.getMes() + " / " + caixaMensal.getAno());
        labelSaldoInicial.setText(String.valueOf(caixaMensal.getSaldoInicial()));
    }
}
