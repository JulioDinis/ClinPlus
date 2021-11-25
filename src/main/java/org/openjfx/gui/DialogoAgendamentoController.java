package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.CreateDialog;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.AgendaMapper;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.AgendaService;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DialogoAgendamentoController implements Initializable {

    private Agenda agenda = new Agenda();

    @FXML
    private JFXTextField jfxTextFieldPaciente;
    @FXML
    private TextArea textAreaObservacao;
    @FXML
    private JFXButton jfxButtonAdicionarPaciente;
    @FXML
    private JFXButton jfxButtonConfirmarAgendamento;
    @FXML
    private JFXButton jfxButtonCancelar;

    @FXML
    private Label labelData;
    @FXML
    private Label labelHorario;
    @FXML
    private TableView<Paciente> tableViewPaciente;
    @FXML
    private TableColumn<Paciente, String> tableColumnNome;

    private AgendaService agendaService;
    private PacienteService pacienteService;
    private Agenda entity = new Agenda();
    private ObservableList<Paciente> obsList;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
    }

    public void setServices(AgendaService agendaService, PacienteService colaboradorService) {
        this.agendaService = agendaService;
        this.pacienteService = colaboradorService;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {

        if (agendaService == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (pacienteService == null) {
            throw new IllegalStateException("Entity was null");
        }

        try {
            AgendaMapper mapper = new AgendaMapper();
            if (this.agenda.getPaciente() != null) {

            } else {
                this.agenda.setPaciente(tableViewPaciente.getSelectionModel().getSelectedItem());
            }
            this.agenda.setObservacao(textAreaObservacao.getText());
            agendaService.saveOrUpdate(agenda);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            //    e.printStackTrace(); it is ok
            //    setErrosMensagens(e.getErrors());

        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro ao Salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }


    public void setAgenda(AgendaDTO evento) {
        AgendaMapper mapper = new AgendaMapper();
        this.agenda = mapper.toEntity(evento);
    }

    public void updateFormData() {
        LocalDate data = this.agenda.getData().toLocalDate();
        String dataFormatada = "" + data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
        labelData.setText(dataFormatada);
        labelHorario.setText(this.agenda.getHorario().toString());
        if (this.agenda.getPaciente() != null) {
            textAreaObservacao.setText(this.agenda.getObservacao());
            jfxTextFieldPaciente.setText(this.agenda.getPaciente().getNome());
            this.tableViewPaciente.setDisable(true);
        }

    }

    public void updateTablePaciente() {
        String busca = this.jfxTextFieldPaciente.getText();
        List<Paciente> list = new ArrayList<>();
        if (busca.isEmpty() || this.jfxTextFieldPaciente.getText().isBlank()) {
            list = pacienteService.findAll();
        } else {
            list = pacienteService.findByName(busca);
            System.out.println(list);
        }
        obsList = FXCollections.observableArrayList(list);
        tableViewPaciente.setItems(obsList);

    }

    @FXML
    public void onTextChange() {
        updateTablePaciente();
    }


    public void subscribeDataChangeListener(AgendaController listener) {
        dataChangeListener.add(listener);
    }

    @FXML
    public void onBtAdicionarAction(ActionEvent event) {
        Paciente paciente = new Paciente();
        Stage parentStage = Utils.currentStage(event);
        CreateDialog createDialog = new CreateDialog();
        createDialog.createDialogForm(paciente, "/org/openjfx/gui/PacienteForm.fxml", (PacienteFormController controller) -> {
            controller.setPaciente(paciente);
            controller.setServices(new PacienteService());
            controller.loadComboBox();
//            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        }, parentStage);

    }

    private void createDialogForm(Paciente paciente, String absolutName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            // Controller
            PacienteFormController controller = loader.getController();
            controller.setPaciente(paciente);
            controller.setServices(new PacienteService());
            controller.loadComboBox();
//            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            // Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Paciente");
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
