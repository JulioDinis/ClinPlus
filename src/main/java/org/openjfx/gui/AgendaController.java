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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.application.MainApp;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.AgendaMapper;
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

    //TODO trocar para Enum
    @FXML
    private TableColumn<AgendaDTO, String> tableColumnStatus;

    @FXML
    private TextArea textAreaDadosProvisorio;
    @FXML
    private JFXButton btnIniciarAtendimento;
    @FXML
    private JFXButton btnCancelarAgendamento;
    @FXML
    private JFXButton btnReagendar;
    @FXML
    private JFXButton btnConfirmarReagendar;
    @FXML
    private JFXButton btnCancelarReagendar;
    @FXML
    private JFXButton btnPacienteAusente;
    @FXML
    private JFXComboBox jfxComboBoxEspecialidadeEspecialista;
    @FXML
    private TextArea textAreaDadosEspecialista;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelEspecialista;
    @FXML
    private Label labelData;
    @FXML
    private Label labelDetalhes;
    @FXML
    private Label labelHorario;
    @FXML
    private TextArea textAreaObservacoes;
    @FXML
    private GridPane gridPaneInformacoes;

    private Colaborador especialista;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private AgendaDTO eventoSelecionado;
    private AgendaDTO eventoReagendado;
    private boolean reagendar = false;

    public void setServices(AgendaService agendaService, ColaboradorService colaboradorService) {
        this.agendaService = agendaService;
        this.colaboradorService = colaboradorService;
    }

    @FXML
    public void onClickMiniCalendario(Event event) {
        if (this.especialista != null) {
            Date data = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            Optional<LocalDate> data2 = miniCalendario.getSelectedDates().stream().findFirst();
            if (data2.isEmpty()) {
            }
            if (data2.isPresent()) {
                this.updateTableView(Utils.convertToDateViaSqlDate(data2.get()));
                this.dataLabel.setText(data2.get().toString());
            }
        }
    }

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
                    if (reagendar) {
                        System.out.println("Reagendar");
                        this.btnConfirmarReagendar.setVisible(true);
                        agenda.setObservacao(this.eventoReagendado.getObservacao());
                        agenda.setIdPaciente(this.eventoReagendado.getIdPaciente());
                        preencherDados(agenda);
                    } else {
                        AgendaDTO agendaDto = tableEventosDoDia.getSelectionModel().getSelectedItem();
                        Stage parentStage = Utils.currentStage(event);
                        createDialogForm(this.especialista, "/org/openjfx/gui/DialogoAgendamento.fxml", parentStage, (DialogoAgendamentoController controller) -> {
                            controller.setServices(new AgendaService(), new PacienteService());
                            controller.setAgenda(agenda);
                            controller.subscribeDataChangeListener(this);
                            controller.updateFormData();
                            controller.updateTablePaciente();
                        }, "Novo Agendamento");
                    }
                } else {
                    System.out.println("Horário Ocupado!");
                    textAreaDadosProvisorio.setText("Não é possível agendar para este horário");
                }
            } else if (event.getClickCount() == 1) {
                if (agenda.getId() == null) {
                    if (reagendar) {
                        System.out.println("Reagendar");
                        this.btnConfirmarReagendar.setVisible(true);
                        this.labelDetalhes.setText("Confirmar Reagendamento?");
                        agenda.setObservacao(this.eventoReagendado.getObservacao());
                        agenda.setIdPaciente(this.eventoReagendado.getIdPaciente());
                        agenda.setPaciente(this.eventoReagendado.getPaciente());
                        preencherDados(agenda);
                    }
                } else {
                    this.labelDetalhes.setText("Detalhes");
                    mostrarBtns(true);
                    this.eventoSelecionado = agenda;
                    preencherDados(agenda);
                    btnIniciarAtendimento.setDisable(false);
                }
            } else {
                System.out.println("Ignorar Multiplos Clicks");
            }
        });
    }

    private void preencherDados(AgendaDTO agenda) {
        if (agenda != null) {
            this.labelDetalhes.setVisible(true);
            LocalDate data = agenda.getData().toLocalDate();
            String dataFormatada = "" + data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
            this.labelNome.setText(agenda.getPaciente());
            this.labelEspecialista.setText(agenda.getEspecialista());
            this.labelData.setText(dataFormatada);
            this.labelHorario.setText(agenda.getHorario().toString());
            this.textAreaObservacoes.setText(agenda.getObservacao());
            this.gridPaneInformacoes.setVisible(true);
        } else {
            this.labelDetalhes.setVisible(false);
            this.labelNome.setText("-");
            this.labelEspecialista.setText("-");
            this.labelData.setText("-");
            this.labelHorario.setText("-");
            this.textAreaObservacoes.setText("-");
            this.gridPaneInformacoes.setVisible(false);
        }
    }

    @FXML
    private void onBtnIniciarAtendimentoClick(ActionEvent event) {
        System.out.println("Iniciar atendimento do paciente");
        //TODO iniciar Atendimento
    }

    @FXML
    private void onBtnCancelarAgendamentoClick(ActionEvent event) {
        System.out.println("Cancelar atendimento ao paciente");
        if (this.eventoSelecionado == null) {
            System.out.println("Está nulo");
        } else {
            agendaService.cancelar(this.eventoSelecionado);
            this.eventoSelecionado = null;
            updateTableView(this.especialista);
        }

    }

    @FXML
    private void onBtnReagendarClick(ActionEvent event) {
        System.out.println("Reagendar paciente");
        AgendaDTO agendaDto = tableEventosDoDia.getSelectionModel().getSelectedItem();
        if (agendaDto.getIdPaciente() != null) {
            this.reagendar = true;
            this.eventoReagendado = agendaDto;
            reagendarBtns(true);
        } else {
            this.reagendar = false;
        }
    }

    @FXML
    private void onBtnConfirmarReagendar(ActionEvent event) {
        AgendaMapper mapper = new AgendaMapper();
        System.out.println("Reagendar paciente");
        agendaService.setReagendado(this.eventoReagendado);
        AgendaDTO agendaDto = tableEventosDoDia.getSelectionModel().getSelectedItem();
        agendaDto.setObservacao(this.eventoReagendado.getObservacao());
        agendaDto.setIdPaciente(this.eventoReagendado.getIdPaciente());
        agendaService.saveOrUpdate(mapper.toEntity(agendaDto));
        reagendar = false;
        this.eventoReagendado = new AgendaDTO();
        reagendarBtns(false);
        updateTableView(this.especialista);
    }

    @FXML
    private void onBtnCancelarReagendarClick(ActionEvent event) {
        reagendar = false;
        this.eventoReagendado = new AgendaDTO();
        reagendarBtns(false);
        updateTableView(this.especialista);
    }

    @FXML
    private void onBtnPacienteAusenteClick(ActionEvent event) {
        System.out.println("Paciente Ausente");
        if (this.eventoSelecionado == null) {
            System.out.println("Está nulo");
        } else {
            agendaService.setAusente(this.eventoSelecionado);
            this.eventoSelecionado = null;
            updateTableView(this.especialista);
        }
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
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
        //Mudando as cores
        tableColumnStatus.setCellFactory(column -> {
            return new TableCell<AgendaDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        AgendaDTO auxAgenda = getTableView().getItems().get(getIndex());

                        if (auxAgenda.getStatus().equals("AUSENTE")) {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: GREY"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("LIVRE")) {
                            setTextFill(Color.BLACK); //The text in red
                            setStyle("-fx-background-color: lightgreen"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("CANCELADO")) {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: RED"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("REAGENDADO")) {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: brown"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("ATENDENDO")) {
                            setTextFill(Color.BLACK); //The text in red
                            setStyle("-fx-background-color: blue"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("ATENDIDO")) {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: lightblue"); //The background of the cell in yellow
                        } else if (auxAgenda.getStatus().equals("AGENDADO")) {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: violet"); //The background of the cell in yellow
                        } else {
                            //Here I see if the row of this cell is selected or not
                            if (getTableView().getSelectionModel().getSelectedItems().contains(auxAgenda))
                                setTextFill(Color.WHITE);
                            else
                                setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        // FIM DAS CORES
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
        obsList = FXCollections.observableArrayList(list);
        tableEventosDoDia.setItems(obsList);
        tableEventosDoDia.refresh();
        this.preencherDados(null);
        this.mostrarBtns(false);
    }

    public void updateTableView(Colaborador colaborador) {
        System.out.println("RECARREGANDO TABELA");
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
        tableEventosDoDia.refresh();
        this.preencherDados(null);
        this.mostrarBtns(false);
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
        obsList = FXCollections.observableArrayList(list);
        tableEventosDoDia.setItems(obsList);
        tableEventosDoDia.refresh();
        this.preencherDados(null);
        this.mostrarBtns(false);
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
        this.reagendar = false;
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

    public void enableAtendimento() {

    }

    public void subscribeDataChangeListener(DataChangeListener listenner) {
        dataChangeListener.add(listenner);
    }

    private void reagendarBtns(Boolean reagendar) {
        this.btnCancelarAgendamento.setVisible(!reagendar);
        this.btnReagendar.setVisible(!reagendar);
        this.btnPacienteAusente.setVisible(!reagendar);
        this.btnIniciarAtendimento.setVisible(!reagendar);
        this.btnConfirmarReagendar.setVisible(false);
        this.btnCancelarReagendar.setVisible(reagendar);
    }

    private void mostrarBtns(Boolean mostrar) {
        this.btnCancelarAgendamento.setVisible(mostrar);
        this.btnReagendar.setVisible(mostrar);
        this.btnPacienteAusente.setVisible(mostrar);
        this.btnIniciarAtendimento.setVisible(mostrar);
        this.btnConfirmarReagendar.setVisible(false);
        this.btnCancelarReagendar.setVisible(this.reagendar);
    }
}
