package org.openjfx.gui;

import javafx.fxml.Initializable;
import org.openjfx.model.service.AgendaService;
import org.openjfx.model.service.FuncionarioService;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaAgendaController implements Initializable {
    private AgendaService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setAgendaService(AgendaService agendaService) {
        this.service = agendaService;
    }

    public void updateAgenda() {
        System.out.println("Carregando AgendaDao.....");
    }
}
