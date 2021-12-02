package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class TelaAtendimentoController implements Initializable {
    private ColaboradorService colaboradorService;
    private PacienteService pacienteService;
    private ItensTratamentoService itensTratamentoService;
    private Colaborador especialistaLogado;
    private AgendaDTO evento;

    @FXML
    private Label labelNomePaciente;

    @FXML
    private JFXButton jfxButtonOrcamento;
    private Paciente paciente;


    public void setFuncionarioService(ColaboradorService colaboradorService) {

    }

    public void updateTableView() {

    }

    public Colaborador getEspecialistaLogado() {
        return especialistaLogado;
    }

    public void setEspecialistaLogado(Colaborador especialistaLogado) {
        this.especialistaLogado = especialistaLogado;
    }

    public void setServices(ColaboradorService colaboradorService, PacienteService pacienteService, ItensTratamentoService itensTratamentoService) {
        this.colaboradorService = colaboradorService;
        this.pacienteService = pacienteService;
        this.itensTratamentoService = itensTratamentoService;
    }

    public void setEvento(AgendaDTO selectedItem) {
        this.evento = selectedItem;
        this.labelNomePaciente.setText(selectedItem.getPaciente().toUpperCase(Locale.ROOT));
        this.paciente = selectedItem.getObjPaciente();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void onJfxButtonOrcamentoClick(ActionEvent event) {
        orçamentoDialogoForm("/org/openjfx/gui/OrcamentoList.fxml", Utils.currentStage(event));
    }

    private void orçamentoDialogoForm(String absolutName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            // Controller
            OrcamentoListController controller = loader.getController();
            controller.setServices(new ItensTratamentoService(), new ProcedimentoService(), new TratamentoService());
            controller.setFuncionarioLogado(this.getEspecialistaLogado());
            controller.setPaciente(this.paciente);
            controller.updateTableView();
            // Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Orçamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (Exception e) {
            System.out.println("ERRO AQUI");
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Erro Loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
