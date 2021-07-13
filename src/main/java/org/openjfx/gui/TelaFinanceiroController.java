package org.openjfx.gui;

import javafx.fxml.Initializable;
import org.openjfx.model.service.FinanceiroService;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaFinanceiroController implements Initializable {
    private FinanceiroService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFinanceiroService(FinanceiroService financeiroService) {
        this.service = financeiroService;
    }

    public void updateTableView() {

    }
}
