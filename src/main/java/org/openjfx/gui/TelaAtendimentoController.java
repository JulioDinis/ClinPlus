package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.AgendaMapper;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.Atendimento;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Tratamento;
import org.openjfx.model.enums.Status;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TelaAtendimentoController implements Initializable {
    private ColaboradorService colaboradorService;
    private PacienteService pacienteService;
    private ItensTratamentoService itensTratamentoService;
    private Colaborador especialistaLogado;
    private AgendaDTO evento;

    private Paciente paciente;
    private ColaboradorService funcionarioService;
    private AtendimentoService atendimentoService;
    private ObservableList<ItensTratamentoDTO> obsList;


    @FXML
    private Label labelNomePaciente;

    @FXML
    private JFXButton jfxButtonOrcamento;
    @FXML
    private TableView<ItensTratamentoDTO> tableViewItensTratamento;
    @FXML
    private TableColumn<ItensTratamentoDTO, String> tableColumnProcedimento;
    @FXML
    private TableColumn<ItensTratamentoDTO, Date> tableColumnDataExecucao;
    @FXML
    private TableColumn<ItensTratamentoDTO, ItensTratamentoDTO> tableColumnRealizado;
    @FXML
    private JFXTextArea jfxTextAreaAnotacoes;
    private Tratamento tratamento;

    public void setFuncionarioService(ColaboradorService colaboradorService) {
        this.funcionarioService = colaboradorService;
    }

    public void updateTableView() {
        if (itensTratamentoService == null) {
            throw new IllegalStateException("Service was Null");
        }
        List<ItensTratamentoDTO> list = itensTratamentoService.findByPacienteId(paciente.getIdPaciente());


        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewItensTratamento.setItems(obsList);
//        initEditButtons();
//        initRemoveButtons();
        initExecutadoButtons();
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
        initializeNodes();
    }

    private synchronized void initializeNodes() {

        tableColumnProcedimento.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        Utils.formatTableColumnDate(tableColumnDataExecucao, "dd/MM/yyyy");
        tableColumnDataExecucao.setCellValueFactory(new PropertyValueFactory<>("dataExecucao"));
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
            controller.updateTableViewProcedimento();
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

    public void initExecutadoButtons() {
        tableColumnRealizado.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRealizado.setCellFactory(param -> new TableCell<ItensTratamentoDTO, ItensTratamentoDTO>() {
            private JFXButton button = new JFXButton("Concluido?", new FontIcon("fa-check"));

            @Override
            protected void updateItem(ItensTratamentoDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                if (obj.isExecutado()) {
                    button.setStyle("-fx-background-color: #7F6");
                    button.setText("Realizado");
                    button.setOnAction(event -> marcarComoNaoRealizado(obj));
                } else {
                    button.setStyle("-fx-background-color: #999");
                    button.setOnAction(event -> marcarComoRealizado(obj));
                }
            }
        });
    }

    private void marcarComoRealizado(@NotNull ItensTratamentoDTO itensTratamentoDTO) {
        System.out.println("###########Realizado########### " + itensTratamentoDTO.toString());
        itensTratamentoService.realizarProcedimento(itensTratamentoDTO);
        updateTableView();
    }

    private void marcarComoNaoRealizado(@NotNull ItensTratamentoDTO itensTratamentoDTO) {
        System.out.println("########### Não Realizado########### " + itensTratamentoDTO.toString());
        itensTratamentoService.desfazerProcedimento(itensTratamentoDTO);
        updateTableView();
    }

    @FXML
    private void finalizarAtendimento() {
        AgendaService agendaService = new AgendaService();
        AgendaMapper agendaMapper = new AgendaMapper();
        AtendimentoService atendimentoService = new AtendimentoService();
        Atendimento atendimento = new Atendimento();
        atendimento.setAgendamento(agendaMapper.toEntity(this.evento));
        atendimento.setAnotacoes(jfxTextAreaAnotacoes.getText());

        atendimento.setTratamento(this.tratamento);

        atendimentoService.salvar(atendimento);
        agendaService.setStatus(this.evento, Status.ATENDIDO.getDescription());

    }
}
