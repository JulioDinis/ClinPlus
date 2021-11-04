package org.openjfx.gui;

import com.calendarfx.view.WeekDayHeaderView;
import com.calendarfx.view.YearMonthView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.application.MainApp;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.AgendaService;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class AgendaController implements Initializable, DataChangeListener {

    AgendaService agendaService = new AgendaService();
    private ObservableList<Colaborador> obsColaboradorEspecialidade;
    ColaboradorService colaboradorService = new ColaboradorService();
    private ObservableList<AgendaDTO> obsList;

    @FXML
    private YearMonthView miniCalendario;
    @FXML
    private WeekDayHeaderView diaDaSemana;
    @FXML
    private Label dataLabel;
    @FXML
    private TableView<AgendaDTO> tableEventosDoDia;
    @FXML
    private TableColumn<AgendaDTO, Time> tableColumnHorario;
    @FXML
    private TableColumn<AgendaDTO, String> tableColumnPaciente;
    @FXML
    private TextArea textAreaDadosProvisorio;
    @FXML
    private JFXButton btnAbrirDialog;
    @FXML
    private JFXComboBox jfxComboBoxEspecialidadeEspecialista;
    @FXML
    private TextArea textAreaDadosEspecialista;
    private Colaborador especialista;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();

    public void setServices(AgendaService agendaService, ColaboradorService colaboradorService) {
        this.agendaService = agendaService;
        this.colaboradorService = colaboradorService;
    }

    public void updateAgenda() {
        // TODO: update Agenda
    }

    @FXML
    public void onClickMiniCalendario(Event event) {
        //  System.out.printf("O que vem no event->" +miniCalendario.getSelectedDates());
        System.out.println("Buscar no bd e mostra na tabela");
        Date data = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        Optional<LocalDate> data2 = miniCalendario.getSelectedDates().stream().findFirst();
        if (data2.isEmpty()) {
        }
        if (data2.isPresent()) {
            this.updateTableView(Utils.convertToDateViaSqlDate(data2.get()));
            this.dataLabel.setText(data2.get().toString());
        }
    }

    // Click do mouse, para click duplo event.getClickCount() ==2
    @FXML
    private void handleRowSelect() {
        // Cria um formatador para a data usando DateFormat:
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar gc = Calendar.getInstance();

        tableEventosDoDia.setOnMouseClicked(event -> {
            AgendaDTO agenda = tableEventosDoDia.getSelectionModel().getSelectedItem();
            System.out.println(agenda);
            if (event.getClickCount() == 2) {
                if (agenda.getId() == null) {
                    Stage parentStage = Utils.currentStage(event);
                    createDialogForm(this.especialista, "/org/openjfx/gui/DialogoAgendamento.fxml", parentStage, (DialogoAgendamentoController controller) -> {
                        controller.setServices(new AgendaService(), new PacienteService());
                        controller.setAgenda(tableEventosDoDia.getSelectionModel().getSelectedItem());
                        controller.subscribeDataChangeListener(this);
                        controller.updateFormData();
                        controller.updateTablePaciente();
                    }, "Novo Agendamento");
                } else {
                    System.out.println("Horário Ocupado!");
                    textAreaDadosProvisorio.setText("Não é possível agendar para este horário");
                }
            } else if (event.getClickCount() == 1) {
                System.out.println("Clicou1");
                if (agenda.getId() == null) {
                    textAreaDadosProvisorio.setText("Este horário está disponível\n ");
                    System.out.println("Cadastra?");
                    btnAbrirDialog.setDisable(true);
                    btnAbrirDialog.setVisible(false);
                } else {
                    textAreaDadosProvisorio.setText(agenda.toString());
                    btnAbrirDialog.setDisable(false);
                    btnAbrirDialog.setVisible(true);
                }
            } else {
                System.out.println("Ignorar Multiplos Clicks");
            }
        });
    }

    @FXML
    private void onBtnAbrirDialogoClick(ActionEvent event) {
        System.out.println("Iniciar atendimento do paciente");
    }

    @FXML
    private void onEspecialistaChange(ActionEvent event) {
        System.out.println("Alterado");
        Colaborador especialista = (Colaborador) jfxComboBoxEspecialidadeEspecialista.getSelectionModel().getSelectedItem();
        Date data = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        this.updateTableView(especialista);
        this.tableEventosDoDia.setDisable(false);
        this.tableEventosDoDia.setOpacity(1D);
        this.especialista = especialista;
    }

    private synchronized void initializeNodes() {
        tableColumnHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        tableColumnPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        //updateTableView();
    }

    public void updateTableView() {
        if (agendaService == null) {
            throw new IllegalStateException("Service was Null");
        }
        Date data = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        List<AgendaDTO> list = agendaService.findByData(data, new Colaborador());
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableEventosDoDia.setItems(obsList);
    }

    public void updateTableView(Colaborador colaborador) {
        if (agendaService == null) {
            throw new IllegalStateException("Service was Null");
        }

        Date data = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        Optional<LocalDate> dataMiniCalendario = miniCalendario.getSelectedDates().stream().findFirst();
        if (dataMiniCalendario.isPresent()) {
            data = Utils.convertToDateViaSqlDate(dataMiniCalendario.get());
        }
        List<AgendaDTO> list = agendaService.findByDataAndEspecialista(data, colaborador);
        obsList = FXCollections.observableArrayList(list);
        tableEventosDoDia.setItems(obsList);
    }

    public void updateTableView(Date data) {
        List<AgendaDTO> list;
        if (agendaService == null) {
            throw new IllegalStateException("Service was Null");
        }
        if (this.especialista == null)
            list = agendaService.findByData(data, new Colaborador());
        else
            list = agendaService.findByDataAndEspecialista(data, this.especialista);
//        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableEventosDoDia.setItems(obsList);
    }

    private synchronized <T> void createDialogForm(Colaborador atendente, String absolutName, Stage parentStage, Consumer<T> initialingAction, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            T controller = loader.getController();
            initialingAction.accept(controller);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
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

    public void loadComboBox() {
        obsColaboradorEspecialidade = FXCollections.observableList(colaboradorService.findAllAtivos());
        jfxComboBoxEspecialidadeEspecialista.setItems(obsColaboradorEspecialidade);
    }

    @Override
    public void onDataChange() {
        updateTableView((Colaborador) jfxComboBoxEspecialidadeEspecialista.getSelectionModel().getSelectedItem());
    }

    @Override
    public void onLogin(Object logado) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {

    }

    public void setEspecialista(Colaborador especialistaLogado) {
        this.especialista = especialistaLogado;
        this.tableEventosDoDia.setDisable(false);
        this.tableEventosDoDia.setOpacity(1D);
        updateTableView(this.especialista);
        jfxComboBoxEspecialidadeEspecialista.getSelectionModel().select(especialista);
        jfxComboBoxEspecialidadeEspecialista.setDisable(true);
    }

    public void enableAtendimento(){

    }

    public void subscribeDataChangeListener(DataChangeListener listenner) {
        dataChangeListener.add(listenner);
    }
}
