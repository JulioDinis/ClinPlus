/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui;


import com.jfoenix.controls.JFXButton;
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
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.ItensTratamentoMapper;
import org.openjfx.mapper.TratamentoMapper;
import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.dto.TratamentoDTO;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.entities.Tratamento;
import org.openjfx.model.service.ItensTratamentoService;
import org.openjfx.model.service.ProcedimentoService;
import org.openjfx.model.service.TratamentoService;

import java.net.URL;
import java.text.DateFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class OrcamentoListController implements Initializable, DataChangeListener {

    private ProcedimentoService procedimentoService;
    private Colaborador colaboradorLogado;
    private TratamentoMapper tratamentoMapper = new TratamentoMapper();
    private ItensTratamentoService itensTratamentoService = new ItensTratamentoService();

    private ObservableList<Procedimento> obsList;
    private ObservableList<ItensTratamentoDto> observableListItensTratamentoDto;
    Map<Procedimento, Integer> procedimentosSelecionados = new HashMap<>();

    private ObservableList<Tratamento> obsSelecionados;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Procedimento> tableViewProcedimento;
    @FXML
    private TableView<ItensTratamentoDto> tableViewItensTratamento;
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
    JFXButton jFxBtImprirOrcamento;

    @FXML

    private List<ItensTratamentoDto> itensTratamentoDtoList = new ArrayList<>();
    private ItensTratamentoService serviceItensTratamento;
    private TratamentoService tratamentoService;
    private TratamentoDTO tratamento;
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
    public void onBtAddAction(ActionEvent event) {
        System.out.println(this.itensTratamentoDtoList);

    }


    @FXML
    public void onTxtBuscaChange() {
        if (procedimentoService == null) {
            throw new IllegalStateException("Service was Null");
        } else {
            List<Procedimento> list;
            if (this.getFuncionarioLogado().getFuncao().equals("Especialista")) {
                list = procedimentoService.findByDescricaoAndId(txtBusca.getText(), this.getFuncionarioLogado().getIdFuncionario());
            } else {
                list = procedimentoService.findByDescricao(txtBusca.getText());
            }
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
        }
    }
    @FXML
    public void onInprimirOrcamentoClick(ActionEvent event){
        Utils.abrirJrxm("/org/openjfx/relatorios/jrxml/Colaboradores2.jrxml");
    }

    // Click do mouse, para click duplo event.getClickCount() ==2
    @FXML
    private void handleRowSelect() {
        // Cria um formatador para a data usando DateFormat:
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar gc = Calendar.getInstance();
        System.out.println(df.format(gc.getTime())); // 14/03/2016
        // Adiciona 10 dias:
        gc.add((GregorianCalendar.DAY_OF_MONTH), 10);

        tableViewProcedimento.setOnMouseClicked(event -> {
                    Procedimento p = new Procedimento();
                    if (event.getClickCount() == 2) {
                        p = tableViewProcedimento.getSelectionModel().getSelectedItem();
                        if (this.tratamento == null) {
                            this.tratamento = new TratamentoDTO();
                            System.out.println("Criou Tratamento");
                            this.tratamento.setValidadeOrcamento(gc.getTime());
                            this.tratamento.setDesconto(0.0);
                            this.tratamento.setTotal(0.0);
                            this.tratamento.setProcedimento(p);
                            this.tratamento.setIdTratamento(tratamentoService.saveOrUpdate(this.tratamento));
                            ItensTratamentoDto itensTratamentoDto = new ItensTratamentoDto();
                            itensTratamentoDto.setTratamento(tratamentoMapper.toEntity(this.tratamento));
                            itensTratamentoDto.setProcedimento(this.tratamento.getProcedimento());
                            itensTratamentoDto.setQuantidade(1);
                            itensTratamentoService.saveOrUpdate(itensTratamentoDto);
//                            atualizaTableItens();
                        } else {
                            if (this.tratamento.getIdTratamento() == null && p.getIdProcedimento() == null) {
                                System.out.println("Nulos");
                            } else {
                                if (itensTratamentoService.findByTratamentoIdAndProcedimentoId(this.tratamento.getIdTratamento(), p.getIdProcedimento()) == null) {
                                    System.out.println("Adicionar a lista");
                                    ItensTratamentoDto itensTratamentoDto = new ItensTratamentoDto();
                                    itensTratamentoDto.setTratamento(tratamentoMapper.toEntity(this.tratamento));
                                    itensTratamentoDto.setProcedimento(p);
                                    itensTratamentoDto.setQuantidade(1);
                                    itensTratamentoService.saveOrUpdate(itensTratamentoDto);

                                } else {

                                    ItensTratamentoDto itensTratamentoDto;
                                    itensTratamentoDto = itensTratamentoService.findByTratamentoIdAndProcedimentoId(this.tratamento.getIdTratamento(), p.getIdProcedimento());
                                    System.out.println("Atualizar a lista");
                                    itensTratamentoDto.setQuantidade(itensTratamentoDto.getQuantidade() + 1);
                                    itensTratamentoService.saveOrUpdate(itensTratamentoDto);
                                }
                            }
                            //                            System.out.println("Atualiza Tratamento");
                        }
                        System.out.println("NOVO ID-> " + this.tratamento.getIdTratamento());
                        atualizaTableItens();
                    }
                }
        );
    }
    private synchronized void atualizaTableItens() {
        if (this.tratamento == null) {
            System.out.println("Nenhum valor");
        } else {
            List<ItensTratamentoDto> list = itensTratamentoService.findByTratamentoId(this.tratamento.getIdTratamento());
            this.observableListItensTratamentoDto = FXCollections.observableArrayList(list);
            this.tableViewItensTratamento.setItems(this.observableListItensTratamentoDto);
        }
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
        if (procedimentoService == null) {
            throw new IllegalStateException("Service was Null");
        }
        if (this.getFuncionarioLogado().getFuncao().equals("Especialista")) {
            List<Procedimento> list = procedimentoService.findByEspecialista(this.getFuncionarioLogado().getIdFuncionario());
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
            this.tableColumnEspecialista.setVisible(false);
        } else {
            List<Procedimento> list = procedimentoService.findAll();
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

    public void setServices(ItensTratamentoService itensTratamentoService, ProcedimentoService procedimentoService, TratamentoService tratamentoService) {
        this.serviceItensTratamento = itensTratamentoService;
        this.procedimentoService = procedimentoService;
        this.tratamentoService = tratamentoService;
    }
}
