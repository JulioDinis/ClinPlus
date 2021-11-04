package org.openjfx.gui;

import com.calendarfx.view.TimeField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.AgendaMapper;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.AgendaService;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DialogoAgendamentoController implements Initializable {

    private Agenda agenda = new Agenda();

    @FXML
    private JFXTextField jfxTextFieldPaciente;
    @FXML
    private JFXButton jfxButtonBuscarPaciente;
    @FXML
    private JFXButton jfxButtonConfirmarAgendamento;
    @FXML
    private JFXButton jfxButtonCancelar;
    @FXML
    private TimeField timeFieldHorario;
    @FXML
    private DatePicker datePickerDataAgendada;
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
            this.agenda.setPaciente(tableViewPaciente.getSelectionModel().getSelectedItem());
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
        System.out.println("GGGGGGGGGGGGGGGGGGGGG" + evento);
    }

    public void updateFormData() {
        datePickerDataAgendada.setValue(this.agenda.getData().toLocalDate());
        timeFieldHorario.setValue(this.agenda.getHorario().toLocalTime());
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
}
