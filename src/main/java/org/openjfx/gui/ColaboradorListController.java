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
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.ColaboradorService;

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
public class ColaboradorListController implements Initializable, DataChangeListener {

    private ColaboradorService service;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Colaborador> tableViewFuncionario;
    @FXML
    private TableColumn<Colaborador, Integer> tableColumnId;

    @FXML
    private TableColumn<Colaborador, String> tableColumnNome;
    // Email, BirthDate, BaseSalary)

    @FXML
    private TableColumn<Colaborador, String> tableColumnEmail;
    @FXML
    private TableColumn<Colaborador, Date> tableColumnDataNascimento;
    @FXML
    private TableColumn<Colaborador, String> tableColumnSexo;

    @FXML
    private TableColumn<Colaborador, String> tableColumnCpf;

    @FXML
    private TableColumn<Colaborador, String> tableColumnCidade;

    @FXML
    private TableColumn<Colaborador, String> tableColumnTelefone;

    @FXML
    private TableColumn<Colaborador, Colaborador> tableColumnEDIT;

    @FXML
    private TableColumn<Colaborador, Colaborador> tableColumnREMOVE;

    @FXML
    private ComboBox<String> comboBoxBuscarPor;

    @FXML
    private Pane paneBusca;

    @FXML
    JFXButton jFxBtNew;

    private ObservableList<Colaborador> obsList;

    // eventos
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Colaborador colaborador = new Colaborador();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(colaborador, "/org/openjfx/gui/FuncionarioForm.fxml", parentStage);
    }
    @FXML
    public void onTxtBuscaChange() {
        System.out.println("Valor Alterado");
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }else{
            List<Colaborador> list = service.findByName(txtBusca.getText());
            System.out.println(list);
            obsList = FXCollections.observableArrayList(list);
            tableViewFuncionario.setItems(obsList);
            initEditButtons();
            initRemoveButtons();
        }
    }

    public void setFuncionarioService(ColaboradorService service) {
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
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
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

        List<Colaborador> list = service.findAllAtivos();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewFuncionario.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Colaborador colaborador, String absolutName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            ColaboradorFormController controller = loader.getController();
            controller.setFuncionario(colaborador);
            controller.setServices(new ColaboradorService(), new ColaboradorService());
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Funcionário");
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
    public void onLogin(Colaborador colaborador) {

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
        tableColumnEDIT.setCellFactory(param -> new TableCell<Colaborador, Colaborador>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Editar", editarIcone);
            @Override
            protected void updateItem(Colaborador obj, boolean empty) {
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
                                obj, "/org/openjfx/gui/FuncionarioForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Colaborador, Colaborador>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));
            @Override
            protected void updateItem(Colaborador obj, boolean empty) {
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

    private void removeEntity(Colaborador colaborador) {
        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o Colaborador?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(colaborador);
                updateTableView();
            } catch (DbIntegrityException e) {
                e.printStackTrace();
                Alerts.showAlert("Erro removing object", null, e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    public void setFuncionarioLogado(Colaborador colaboradorLogado) {
        System.out.println("OII");
    }
}
