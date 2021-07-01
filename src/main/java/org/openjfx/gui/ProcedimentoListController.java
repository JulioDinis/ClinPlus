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
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.service.FuncionarioService;
import org.openjfx.model.service.PacienteService;
import org.openjfx.model.service.ProcedimentoService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class ProcedimentoListController implements Initializable, DataChangeListener {

    private ProcedimentoService service;
    private Funcionario funcionarioLogado;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Procedimento> tableViewProcedimento;
    @FXML
    private TableColumn<Procedimento, Integer> tableColumnId;

    @FXML
    private TableColumn<Procedimento, String> tableCollumDescricao;
    // Email, BirthDate, BaseSalary)

    @FXML
    private TableColumn<Procedimento, Double> tableColumValor;

    @FXML
    private TableColumn<Procedimento, Procedimento> tableColumnEDIT;

    @FXML
    private TableColumn<Procedimento, Procedimento> tableColumnREMOVE;


    @FXML
    private Button btNew;

    @FXML
    JFXButton jFxBtNew;

    @FXML
    private FontIcon jFXImVieBtnAlternar;

    private ObservableList<Procedimento> obsList;

    // eventos
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Procedimento procedimento = new Procedimento();
        procedimento.setIdEspecialista(funcionarioLogado.getIdFuncionario());
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(procedimento, "/org/openjfx/gui/ProcedimentoForm.fxml", parentStage);

    }

    @FXML
    public void onTxtBuscaChange() {
        System.out.println("Valor Alterado");
    }

    @FXML
    public void onJFXImVieBtnAlternaClick() {

        if (jFXImVieBtnAlternar.getIconLiteral().equals("fa-toggle-off")) {
            jFXImVieBtnAlternar.setIconLiteral("fa-toggle-on");
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }

            List<Procedimento> list = service.findAll();
            System.out.println(list);
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
            initEditButtons();
            initRemoveButtons();

        } else {
            jFXImVieBtnAlternar.setIconLiteral("fa-toggle-off");
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }

            List<Procedimento> list = service.findAllAtivos();
            System.out.println(list);
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
            initEditButtons();
            initRemoveButtons();
        }

    }

    public void setProcedimentoService(ProcedimentoService service) {
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
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idProcedimento"));
        tableCollumDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        Stage stage = (Stage) MainApp.getMainScene().getWindow();

        tableViewProcedimento.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }

        List<Procedimento> list = service.findAllAtivos();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        tableViewProcedimento.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Procedimento procedimento, String absolutName, Stage parentStage) {
        try {
            System.out.println("errossss");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();

            ProcedimentoForm controller = loader.getController();
            controller.setEntity(procedimento);
            controller.setService(new ProcedimentoService(), new ProcedimentoService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Procedimento");
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
    public void onLogin(Funcionario funcionario) {
        throw new IllegalStateException("Service was Null");
    }

    /**
     * Método para colocar um button dentro da tabela
     */
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Procedimento, Procedimento>() {
            private final FontIcon editarIcone = new FontIcon("fa-edit");
            private final JFXButton button = new JFXButton("Editar", editarIcone);

            @Override
            protected void updateItem(Procedimento obj, boolean empty) {
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
                                obj, "/org/openjfx/gui/ProcedimentoForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Procedimento, Procedimento>() {
            private JFXButton button = new JFXButton("Excluir", new FontIcon("fa-remove"));

            @Override
            protected void updateItem(Procedimento obj, boolean empty) {
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

    private void removeEntity(Procedimento procedimento) {

        Optional<ButtonType> result = Alerts.
                showConfirmation("Confirmation", "Tem certeza que deseja excluir o Paciente?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was Null");
            }
            try {
                service.remove(procedimento);
                updateTableView();
            } catch (DbIntegrityException e) {
                e.printStackTrace();
                Alerts.showAlert("Erro removing object", null, e.getMessage(), Alert.AlertType.INFORMATION);
            }

        }
    }

    public Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }
}
