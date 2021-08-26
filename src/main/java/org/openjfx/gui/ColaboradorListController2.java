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
import org.openjfx.mapper.ColaboradorMapper;
import org.openjfx.model.dao.ColaboradorDao;
import org.openjfx.model.dto.ColaboradorDTO;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.AtendenteService;
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
public class ColaboradorListController2 implements Initializable, DataChangeListener {

    private AtendenteService service;
    private ColaboradorMapper mapper = new ColaboradorMapper();

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<ColaboradorDTO> tableViewFuncionario;
    @FXML
    private TableColumn<ColaboradorDTO, Integer> tableColumnId;

    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnNome;
    // Email, BirthDate, BaseSalary)

    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnEmail;
    @FXML
    private TableColumn<ColaboradorDTO, Date> tableColumnDataNascimento;
    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnSexo;

    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnCpf;

    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnCidade;

    @FXML
    private TableColumn<ColaboradorDTO, String> tableColumnTelefone;

    @FXML
    private TableColumn<ColaboradorDTO, ColaboradorDTO> tableColumnEDIT;

    @FXML
    private TableColumn<ColaboradorDTO, ColaboradorDTO> tableColumnREMOVE;

    @FXML
    private ComboBox<String> comboBoxBuscarPor;

    @FXML
    private Pane paneBusca;

    @FXML
    JFXButton jFxBtNew;

    private ObservableList<ColaboradorDTO> obsList;

    // eventos
    @FXML
    public void onBtNewEspecialistaAction(ActionEvent event) {
        ColaboradorDTO colaborador = new ColaboradorDTO();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(null, "/org/openjfx/gui/FuncionarioForm.fxml", parentStage, (ColaboradorFormController controller) -> {
            controller.setFuncionario(null);
            controller.setServices(new ColaboradorService(), new ColaboradorService());
            controller.loadComboBox();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        }, "Especialista");
    }

    @FXML
    public void onBtNewAtendenteAction(ActionEvent event) {
        ColaboradorDTO colaborador = new ColaboradorDTO();
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(null, "/org/openjfx/gui/AtendenteForm.fxml", parentStage, (AtendenteFormController controller) -> {
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
            List<ColaboradorDTO> list = service.findByName(txtBusca.getText());
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

        List<ColaboradorDTO> list = service.findAll();
        System.out.println("Buscando...");
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewFuncionario.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private synchronized <T> void createDialogForm(Colaborador colaborador, String absolutName, Stage parentStage, Consumer<T> initialingAction, String title) {
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
        tableColumnEDIT.setCellFactory(param -> new TableCell<ColaboradorDTO, ColaboradorDTO>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Editar", editarIcone);

            @Override
            protected void updateItem(ColaboradorDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                // coloca o botão na tabela
                setGraphic(button);
                // seta a action do button
                button.setOnAction(
                        event -> createDialogForm(mapper.toEntityColaborador(obj),"/org/openjfx/gui/FuncionarioForm.fxml", Utils.currentStage(event), (ColaboradorFormController controller) -> {
                            controller.setFuncionario(mapper.toEntityColaborador(obj));
                            controller.setServices(new ColaboradorService(), new ColaboradorService());
                            controller.loadComboBox();
                            controller.updateFormData();
                        }, "Editar"));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<ColaboradorDTO, ColaboradorDTO>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));

            @Override
            protected void updateItem(ColaboradorDTO obj, boolean empty) {
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

    private void removeEntity(ColaboradorDTO colaborador) {
        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o Colaborador?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(mapper.toEntityAtendente(colaborador));
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
