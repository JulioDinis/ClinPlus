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
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.service.AtendenteService;
import org.openjfx.model.service.AtendenteService;

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
public class AtendenteListController implements Initializable, DataChangeListener {

    private AtendenteService service;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Atendente> tableViewFuncionario;
    @FXML
    private TableColumn<Atendente, Integer> tableColumnId;

    @FXML
    private TableColumn<Atendente, String> tableColumnNome;
    // Email, BirthDate, BaseSalary)

    @FXML
    private TableColumn<Atendente, String> tableColumnEmail;
    @FXML
    private TableColumn<Atendente, Date> tableColumnDataNascimento;
    @FXML
    private TableColumn<Atendente, String> tableColumnSexo;

    @FXML
    private TableColumn<Atendente, String> tableColumnCpf;

    @FXML
    private TableColumn<Atendente, String> tableColumnCidade;

    @FXML
    private TableColumn<Atendente, String> tableColumnTelefone;

    @FXML
    private TableColumn<Atendente, Atendente> tableColumnEDIT;

    @FXML
    private TableColumn<Atendente, Atendente> tableColumnREMOVE;

    @FXML
    private ComboBox<String> comboBoxBuscarPor;

    @FXML
    private Pane paneBusca;

    @FXML
    JFXButton jFxBtNew;

    private ObservableList<Atendente> obsList;

    // eventos
    @FXML
    public void onBtNewEspecialistaAction(ActionEvent event) {
        Atendente atendente = new Atendente();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(atendente, "/org/openjfx/gui/FuncionarioForm.fxml", parentStage, (AtendenteFormController controller) -> {
            controller.setFuncionario(atendente);
            controller.setServices(new AtendenteService(), new AtendenteService());
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        }, "Especialista");
    }

    @FXML
    public void onBtNewAtendenteAction(ActionEvent event) {
        Atendente atendente = new Atendente();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(atendente, "/org/openjfx/gui/AtendenteForm.fxml", parentStage, (AtendenteFormController controller) -> {
            controller.setFuncionario(new Atendente());
            controller.setServices(new AtendenteService(), new AtendenteService());
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        }, "Atendente");
    }

    @FXML
    public void onTxtBuscaChange() {
        System.out.println("Valor Alterado");
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        } else {
            List<Atendente> list = service.findByName(txtBusca.getText());
            System.out.println(list);
            obsList = FXCollections.observableArrayList(list);
            tableViewFuncionario.setItems(obsList);
            initEditButtons();
            initRemoveButtons();
        }
    }

    public void setFuncionarioService(AtendenteService service) {
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
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idAtendente"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        tableColumnCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        // Formatar a data usando o método do utils
        Utils.formatTableColumnDate(tableColumnDataNascimento, "dd/MM/yyyy");
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }

        List<Atendente> list = service.findAllAtivos();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewFuncionario.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    //    private void createDialogForm(Atendente atendente, String absolutName, Stage parentStage) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
//            Pane pane = loader.load();
//            AtendenteFormController controller = loader.getController();
//            controller.setFuncionario(atendente);
//            controller.setServices(new AtendenteService(), new AtendenteService());
//            controller.loadComboBox();
//            controller.subscribeDataChangeListener(this);
//            controller.updateFormData();
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Insira os dados do Funcionário");
//            dialogStage.setScene(new Scene(pane));
//            dialogStage.setResizable(false);
//            dialogStage.initOwner(parentStage);
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.showAndWait();
//        } catch (Exception e) {
//            System.out.println("ERRO AQUI");
//            e.printStackTrace();
//            Alerts.showAlert("IO Exception", "Erro Loading view", e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
    private synchronized <T> void createDialogForm(Atendente atendente, String absolutName, Stage parentStage, Consumer<T> initialingAction, String title) {
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

    @Override
    public void onDataChange() {
        updateTableView();
    }

    @Override
    public void onLogin(Object obj) {

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
        tableColumnEDIT.setCellFactory(param -> new TableCell<Atendente, Atendente>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Editar", editarIcone);
            @Override
            protected void updateItem(Atendente obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                // coloca o botão na tabela
                setGraphic(button);
                // seta a action do button
                button.setOnAction(
//                        event -> createDialogForm(
//                                obj, "/org/openjfx/gui/FuncionarioForm.fxml", Utils.currentStage(event)));
                        (event) -> createDialogForm(obj, "/org/openjfx/gui/AtendenteForm.fxml", Utils.currentStage(event), (AtendenteFormController controller) -> {
                            controller.setFuncionario(obj);
                            controller.setServices(new AtendenteService(), new AtendenteService());
                            controller.loadComboBox();
                            controller.updateFormData();
                          }, "Editar"));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Atendente, Atendente>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));

            @Override
            protected void updateItem(Atendente obj, boolean empty) {
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

    private void removeEntity(Atendente atendente) {
        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o Atendente?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(atendente);
                updateTableView();
            } catch (DbIntegrityException e) {
                e.printStackTrace();
                Alerts.showAlert("Erro removing object", null, e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    public void setFuncionarioLogado(Atendente atendenteLogado) {
        System.out.println("OII");
    }
}
