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
import org.openjfx.model.entities.ItensTratamento;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;
import org.openjfx.model.service.ProcedimentoService;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class OrcamentoListController implements Initializable, DataChangeListener {

    private ProcedimentoService service;
    private Colaborador colaboradorLogado;
    private ObservableList<Procedimento> obsList;
    private ObservableList<ItensTratamento> observableListItensTratamento;
    Map<Procedimento, Integer> procedimentosSelecionados = new HashMap<>();

    private ObservableList<Tratamento> obsSelecionados;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Procedimento> tableViewProcedimento;
    @FXML
    private TableView<ItensTratamento> tableViewItensTratamento;
    @FXML
    private TableColumn<Procedimento, Integer> tableColumnEspecialista;
    @FXML
    private TableColumn<Procedimento, String> tableCollumDescricao;
    @FXML
    private TableColumn<Procedimento, String> tableColumnProcedimento;
    @FXML
    private TableColumn<Procedimento, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<Procedimento, Double> tableColumValor;
    @FXML
    JFXButton jFxBtNew;

    @FXML
    private FontIcon jFXImVieBtnAlternar;
    private List<ItensTratamento> itensTratamentoList = new ArrayList<>();
    ;


    // eventos
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Procedimento procedimento = new Procedimento();
        procedimento.setIdEspecialista(colaboradorLogado.getIdFuncionario());
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(procedimento, "/org/openjfx/gui/ProcedimentoForm.fxml", parentStage);

    }

    @FXML
    public void onTxtBuscaChange() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        } else {
            List<Procedimento> list;
            if (this.getFuncionarioLogado().getFuncao().equals("Especialista")) {
                list = service.findByDescricaoAndId(txtBusca.getText(), this.getFuncionarioLogado().getIdFuncionario());
            } else {
                list = service.findByDescricao(txtBusca.getText());
            }
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
        }
    }

    // Click do mouse, para click duplo event.getClickCount() ==2
    @FXML
    private void handleRowSelect() {
        tableViewProcedimento.setOnMouseClicked(event ->
                this.inserirLista(
                        tableViewProcedimento
                                .getSelectionModel()
                                .getSelectedItem(),
                        1)
        );

    }

    private void  inserirLista(Procedimento procedimento, Integer quantidade) {
        if (this.itensTratamentoList.size() > 0) {
            for (ItensTratamento it : this.itensTratamentoList) {
                if (it.getProcedimento().equals(procedimento)) {
                    Integer qtd = it.getQuantidade();
                    quantidade = quantidade + qtd;
                    this.itensTratamentoList.remove(it);
                }
            }
        }
        this.itensTratamentoList.add(new ItensTratamento(procedimento, quantidade));


        this.atualizaTableItens();


    }

    private void atualizaTableItens() {


        observableListItensTratamento = FXCollections.observableArrayList(itensTratamentoList);
        this.tableViewItensTratamento.setItems(observableListItensTratamento);

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

        // tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idProcedimento"));
        tableCollumDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnProcedimento.setCellValueFactory(new PropertyValueFactory<Procedimento, String>("descricao"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        //   tableColumnEspecialista.setCellValueFactory(new PropertyValueFactory<>("idEspecialista"));

    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }
        if (this.getFuncionarioLogado().getFuncao().equals("Especialista")) {
            List<Procedimento> list = service.findByEspecialista(this.getFuncionarioLogado().getIdFuncionario());
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
            this.tableColumnEspecialista.setVisible(false);
        } else {
            List<Procedimento> list = service.findAll();
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
        }
    }

    private void createDialogForm(Procedimento procedimento, String absolutName, Stage parentStage) {
        try {
            System.out.println("errossss");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            ProcedimentoFormController controller = loader.getController();
            controller.setEntity(procedimento);
            controller.setColaborador(this.colaboradorLogado);
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
    public void onLogin(Colaborador colaborador) {
        throw new IllegalStateException("Service was Null");
    }

    @Override
    public void onLogout() {
    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {
    }

    public Colaborador getFuncionarioLogado() {
        return colaboradorLogado;
    }

    public void setFuncionarioLogado(Colaborador colaboradorLogado) {
        this.colaboradorLogado = colaboradorLogado;
    }
}
