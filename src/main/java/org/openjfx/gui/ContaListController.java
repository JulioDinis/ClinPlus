/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui;


import com.jfoenix.controls.JFXButton;
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
import org.kordamp.ikonli.javafx.FontIcon;
import org.openjfx.application.MainApp;
import org.openjfx.db.DbIntegrityException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.ContaService;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class ContaListController implements Initializable, DataChangeListener {
    private ContaService service;
    private ObservableList<ContaDTO> obsList;
    private CaixaMensal caixaMensal;

    @FXML
    private TextField txtBusca;
    @FXML
    private TableView<ContaDTO> tableViewContaDTO;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataCadastro;
    @FXML
    private TableColumn<ContaDTO, String> tableColumnDescricao;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValor;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataVencimento;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataPagamento;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValorPago;
    @FXML
    private TableColumn<ContaDTO, String> tableColumnObservacao;

    @FXML
    private TableColumn<ContaDTO, ContaDTO> tableColumnEDIT;
    @FXML
    private TableColumn<ContaDTO, ContaDTO> tableColumnREMOVE;

    @FXML
    private TableColumn<ContaDTO, ContaDTO> tableColumnPAGAR;


    // eventos
    @FXML
    public void onBtNewAction(ActionEvent event) {
        ContaDTO contaDTO = new ContaDTO();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(contaDTO, "/org/openjfx/gui/ContaForm.fxml", (ContaFormController controller) -> {
            controller.setEntity(contaDTO);
            controller.setServices(new ContaService());
            controller.setCaixaMensal(this.caixaMensal);
//            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
//            controller.updateFormData();
        }, parentStage);

    }

    @FXML
    public void onJfxButtonBuscarClick(ActionEvent event) {
        System.out.println("Buscar por: -> " + txtBusca.getText());
    }


    @FXML
    public void onTxtBuscaChange() {
//        System.out.println("Valor Alterado");
//        if (service == null) {
//            throw new IllegalStateException("Service was Null");
//        } else {
//            List<ContaDTO> list = service.findByName(txtBusca.getText());
//            System.out.println(list);
//            obsList = FXCollections.observableArrayList(list);
//            tableViewContaDTO.setItems(obsList);
//            initEditButtons();
//            initRemoveButtons();
//        }
    }

    public void setContaService(ContaService service) {
        this.service = service;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale ptBr = new Locale("pt", "BR");
        Locale.setDefault(ptBr);
        initializeNodes();
    }

    private synchronized void initializeNodes() {
        tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnDataVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        tableColumnDataPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        tableColumnValorPago.setCellValueFactory(new PropertyValueFactory<>("valorPagamento"));
        tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        Utils.formatTableColumnDouble(tableColumnValor, 2);
        Utils.formatTableColumnDouble(tableColumnValorPago, 2);
        Utils.formatTableColumnDate(tableColumnDataCadastro, "dd/MM/yyyy");
        Utils.formatTableColumnDate(tableColumnDataVencimento, "dd/MM/yyyy");
        Utils.formatTableColumnDate(tableColumnDataPagamento, "dd/MM/yyyy");
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
        tableViewContaDTO.prefHeightProperty().bind(stage.heightProperty());
    }


    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }
        List<ContaDTO> list = service.findAll();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewContaDTO.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
        initPagarButton();
    }

    private void createDialogForm(ContaDTO contaDTO, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/gui/ContaForm.fxml"));
            Pane pane = loader.load();
            // Controller
            ContaFormController controller = loader.getController();
            controller.setEntity(contaDTO);
            controller.setCaixaMensal(this.caixaMensal);
            controller.setServices(new ContaService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            // Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados da Despesa");
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

    @Override
    public void onDataChange() {
        updateTableView();
    }

    @Override
    public void onLogin(Object obj) {
        throw new IllegalStateException("Service was Null");
    }

    @Override
    public void onCaixaAbertoChange(CaixaMensal caixaAberto) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {

    }

    /**
     * Método para colocar um button dentro da tabela
     */
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Editar", editarIcone);
            @Override
            protected void updateItem(ContaDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                // coloca o botão na tabela
                setGraphic(button);
                // seta a action do button
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event)));
            }
        });
    }

    private void initPagarButton() {
        tableColumnPAGAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnPAGAR.setCellFactory(param -> new TableCell<>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Pagar", editarIcone);

            @Override
            protected void updateItem(ContaDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                // coloca o botão na tabela
                setGraphic(button);
                button.setDisable(true);
                // seta a action do button
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));
            @Override
            protected void updateItem(ContaDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(ContaDTO contaDTO) {
        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o ContaDTO?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(contaDTO);
                updateTableView();
            } catch (DbIntegrityException e) {
                e.printStackTrace();
                Alerts.showAlert("Erro removing object", null, e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    public void setCaixaAberto(CaixaMensal caixaAberto) {
        this.caixaMensal = caixaAberto;
    }

    public <T> void createDialogForm(ContaDTO contaDTO, String absolutName, Consumer<T> initialingAction, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            T controller = loader.getController();
            initialingAction.accept(controller);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Aporte");
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
