package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.openjfx.application.ToolbarActionCallBack;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.util.ResourceBundle;

public class ToolbarController implements Initializable {

    @FXML
    private JFXButton jFXbutPaciente;

    @FXML
    private JFXButton jFXButEspecialista;

    @FXML
    private JFXButton jFXButProcedimento;

    @FXML
    private JFXButton jFXButRelatorio;

    private ToolbarActionCallBack callBack;

    @FXML
    void onJFXButtonEspecialistaAction(ActionEvent event) {

    }

    @FXML
    void onJFXButtonPacienteAction(ActionEvent event) {

        System.out.println("Clicou em paciente");
        callBack.buttonAction("/org/openjfx/gui/PacienteList.fxml", (PacienteListController controller) -> {
            controller.setPacienteService(new PacienteService());
            controller.updateTableView();
        });

    }

    @FXML
    void abrirMenu(ActionEvent event) {
        JFXButton buttonClicked = (JFXButton) event.getSource();
        callBack.buttonAction("/org/openjfx/gui/Menu.fxml", (MenuController controller) -> {
            controller.setTipo(buttonClicked.getAccessibleText());
        });
    }

    @FXML
    void onJFXButtonProcedimentoAction(ActionEvent event) {

    }

    @FXML
    void onJFXButtonRelatorioAction(ActionEvent event) {

    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setActionCallBack(ToolbarActionCallBack callBack) {
        this.callBack = callBack;
    }
}
