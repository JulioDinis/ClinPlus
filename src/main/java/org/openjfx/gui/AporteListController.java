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
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.AporteService;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class AporteListController implements Initializable, DataChangeListener {
    private AporteService service;
    private ObservableList<AporteDTO> obsList;
    private CaixaMensal caixaMensal;

    @FXML
    private TextField txtBusca;
    @FXML
    private TableView<AporteDTO> tableViewAporteDTO;
    @FXML
    private TableColumn<AporteDTO, String> tableColumnDescricao;
    @FXML
    private TableColumn<AporteDTO, String> tableColumnColaborador;
    @FXML
    private TableColumn<AporteDTO, Date> tableColumnData;
    @FXML
    private TableColumn<AporteDTO, Double> tableColumnAporte;
    @FXML
    private TableColumn<AporteDTO, AporteDTO> tableColumnEDIT;
    @FXML
    private TableColumn<AporteDTO, AporteDTO> tableColumnREMOVE;



    // eventos
    @FXML
    public void onBtNewAction(ActionEvent event) {
        AporteDTO aporteDTO = new AporteDTO();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(aporteDTO, "/org/openjfx/gui/AporteForm.fxml", (AporteFormController controller) -> {
            controller.setEntity(aporteDTO);
            controller.setServices(new AporteService());
            controller.setCaixaMensal(this.caixaMensal);
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
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
//            List<AporteDTO> list = service.findByName(txtBusca.getText());
//            System.out.println(list);
//            obsList = FXCollections.observableArrayList(list);
//            tableViewAporteDTO.setItems(obsList);
//            initEditButtons();
//            initRemoveButtons();
//        }
    }

    public void setAporteDTOService(AporteService service) {
        this.service = service;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private synchronized void initializeNodes() {
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnAporte.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnColaborador.setCellValueFactory(new PropertyValueFactory<>("colaboradorNome"));
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
//        // Formatar a data usando o método do utils
        Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");

//        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
//        // Formata o Salario usando o método do utils
//        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
        Stage stage = (Stage) MainApp.getMainScene().getWindow();

        tableViewAporteDTO.prefHeightProperty().bind(stage.heightProperty());

    }


    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }
        List<AporteDTO> list = service.findAll();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewAporteDTO.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(AporteDTO aporteDTO, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/gui/AporteForm.fxml"));
            Pane pane = loader.load();
            // Controller
            AporteFormController controller = loader.getController();
            controller.setEntity(aporteDTO);
            controller.setCaixaMensal(this.caixaMensal);
            controller.setServices(new AporteService());
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            // Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do aporteDTO");
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
            protected void updateItem(AporteDTO obj, boolean empty) {
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

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));

            @Override
            protected void updateItem(AporteDTO obj, boolean empty) {
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

    private void removeEntity(AporteDTO aporteDTO) {
        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o AporteDTO?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(aporteDTO);
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

    public <T> void createDialogForm(AporteDTO aporteDTO, String absolutName, Consumer<T> initialingAction, Stage parentStage) {
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
