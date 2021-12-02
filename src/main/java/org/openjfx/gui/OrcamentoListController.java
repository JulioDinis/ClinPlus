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
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.mapper.ProcedimentoMapper;
import org.openjfx.mapper.TratamentoMapper;
import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.dto.ProcedimentoDTO;
import org.openjfx.model.dto.TratamentoDTO;
import org.openjfx.model.entities.*;
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
    private Object colaboradorLogado;
    private TratamentoMapper tratamentoMapper = new TratamentoMapper();
    private ItensTratamentoService itensTratamentoService = new ItensTratamentoService();

    private ObservableList<ProcedimentoDTO> obsList;
    private ObservableList<ItensTratamentoDTO> observableListItensTratamentoDTO;
    Map<Procedimento, Integer> procedimentosSelecionados = new HashMap<>();

    private ObservableList<Tratamento> obsSelecionados;

    @FXML
    private TextField txtBusca;
    @FXML
    private TextField txtDesconto;

    @FXML
    private Label valorBruto;
    @FXML
    private Label total;

    @FXML
    private TableView<ProcedimentoDTO> tableViewProcedimento;
    @FXML
    private TableView<ItensTratamentoDTO> tableViewItensTratamento;
    @FXML
    private TableColumn<ProcedimentoDTO, Integer> tableColumnEspecialista;
    @FXML
    private TableColumn<ProcedimentoDTO, String> tableCollumDescricao;
    @FXML
    private TableColumn<ProcedimentoDTO, String> tableColumnProcedimento;
    @FXML
    private TableColumn<ProcedimentoDTO, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<ProcedimentoDTO, Double> tableColumValor;
    @FXML
    JFXButton jFxBtNew;
    @FXML
    JFXButton jFxBtImprirOrcamento;

    @FXML

    private List<ItensTratamentoDTO> itensTratamentoDTOList = new ArrayList<>();
    private ItensTratamentoService serviceItensTratamento;
    private TratamentoService tratamentoService;
    private TratamentoDTO tratamento;
    private Paciente paciente;
    ;


    // eventos
//    @FXML
//    public void onBtNewAction(ActionEvent event) {
//        Procedimento procedimento = new Procedimento();
//        procedimento.setColaborador(colaboradorLogado);
//        Stage parentStage = Utils.currentStage(event);
//        createDialogForm(procedimento, "/org/openjfx/gui/ProcedimentoForm.fxml", parentStage);
//
//    }

    @FXML
    public void onBtAddAction(ActionEvent event) {
        System.out.println(this.itensTratamentoDTOList);

    }


    @FXML
    public void onTxtBuscaChange() {
        if (procedimentoService == null) {
            throw new IllegalStateException("Service was Null");
        } else {
            List<ProcedimentoDTO> list;
            if (this.getFuncionarioLogado() instanceof Colaborador) {
                list = procedimentoService.findByDescricaoAndId(txtBusca.getText(), ((Colaborador) this.getFuncionarioLogado()).getIdColaborador());
            } else {
                list = procedimentoService.findByDescricao(txtBusca.getText());
            }
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
        }
    }

    @FXML
    public void atualizaValores() {
        if (procedimentoService == null) {
            throw new IllegalStateException("Service was Null");
        } else {
            List<ItensTratamentoDTO> list = itensTratamentoService.findByTratamentoId(this.tratamento.getIdTratamento());
            Double total = 0d;
            Double valorComDesconto = 0d;
            for (ItensTratamentoDTO it : list) {
                total = total + (it.getValor() * it.getQuantidade());
            }
            this.valorBruto.setText(total.toString());
            if (txtDesconto.getText().isBlank() || txtDesconto.getText().isEmpty()) {
                valorComDesconto = total;
            } else {
                valorComDesconto = total - Double.parseDouble(this.txtDesconto.getText());
            }
            this.total.setText(valorComDesconto.toString());
            this.tratamento.setDesconto(total - valorComDesconto);
            this.tratamento.setTotal(total);
            tratamentoService.saveOrUpdate(this.tratamento);
        }
    }

    @FXML
    public void onTxtDescontoChange() {
        if (this.tratamento == null) {
            System.out.println("Tratamento Invalido");
        } else {
            this.atualizaValores();
        }
    }

    @FXML
    public void onInprimirOrcamentoClick(ActionEvent event) {
        Integer codigo = null;
        if (this.tratamento != null) {
            this.tratamento.setTotal(Double.parseDouble(this.valorBruto.getText()));
            this.tratamento.setDesconto(Double.parseDouble(this.txtDesconto.getText()));
            tratamentoService.saveOrUpdate(this.tratamento);
            codigo = this.tratamento.getIdTratamento();
            Utils.abrirJrxm("/org/openjfx/relatorios/jrxml/orcamento.jrxml", codigo);
        }
    }

    // Click do mouse, para click duplo event.getClickCount() ==2
    @FXML
    private void handleRowSelect() {
        // Cria um formatador para a data usando DateFormat:
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar gc = Calendar.getInstance();
        System.out.println(df.format(gc.getTime()));
        // Adiciona 10 dias:
        gc.add((GregorianCalendar.DAY_OF_MONTH), 10);
        tableViewProcedimento.setOnMouseClicked(event -> {
                    Procedimento p = new Procedimento();
                    if (event.getClickCount() == 2) {
                        ProcedimentoMapper mapper = new ProcedimentoMapper();
                        p = mapper.toEntity(tableViewProcedimento.getSelectionModel().getSelectedItem());
                        if (this.tratamento == null) {
                            this.tratamento = new TratamentoDTO();
                            this.tratamento.setPaciente(this.paciente);
                            System.out.println("Criou Tratamento");
                            this.tratamento.setValidadeOrcamento(gc.getTime());
                            this.tratamento.setDesconto(0.0);
                            this.tratamento.setTotal(0.0);
                            this.tratamento.setProcedimento(p);
                            this.tratamento.setIdTratamento(tratamentoService.saveOrUpdate(this.tratamento));
                            ItensTratamentoDTO itensTratamentoDto = new ItensTratamentoDTO();
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
                                    ItensTratamentoDTO itensTratamentoDto = new ItensTratamentoDTO();
                                    itensTratamentoDto.setTratamento(tratamentoMapper.toEntity(this.tratamento));
                                    itensTratamentoDto.setProcedimento(p);
                                    itensTratamentoDto.setQuantidade(1);
                                    itensTratamentoService.saveOrUpdate(itensTratamentoDto);
                                } else {
                                    ItensTratamentoDTO itensTratamentoDto;
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
                        atualizaValores();
                    }
                }
        );
    }

    private synchronized void atualizaTableItens() {
        if (this.tratamento == null) {
            System.out.println("Nenhum valor");
        } else {
            List<ItensTratamentoDTO> list = itensTratamentoService.findByTratamentoId(this.tratamento.getIdTratamento());
            this.observableListItensTratamentoDTO = FXCollections.observableArrayList(list);
            this.tableViewItensTratamento.setItems(this.observableListItensTratamentoDTO);
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
        tableColumnProcedimento.setCellValueFactory(new PropertyValueFactory<ProcedimentoDTO, String>("descricao"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        //   tableColumnEspecialista.setCellValueFactory(new PropertyValueFactory<>("idEspecialista"));

    }

    public void updateTableView() {
        if (procedimentoService == null) {
            throw new IllegalStateException("Service was Null");
        }
        if (this.getFuncionarioLogado() instanceof Colaborador) {
            List<ProcedimentoDTO> list = procedimentoService.findByEspecialista(((Colaborador) this.getFuncionarioLogado()).getIdColaborador());
            obsList = FXCollections.observableArrayList(list);
            tableViewProcedimento.setItems(obsList);
            this.tableColumnEspecialista.setVisible(false);
            System.out.println("Colaborador");
        } else {
            System.out.println("Outro -> " + this.getFuncionarioLogado().getClass());
            List<ProcedimentoDTO> list = procedimentoService.findAll();
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
            controller.setColaborador((Colaborador) this.colaboradorLogado);
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

    public Object getFuncionarioLogado() {
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

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
