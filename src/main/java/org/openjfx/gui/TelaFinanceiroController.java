package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.openjfx.application.MainApp;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.AporteService;
import org.openjfx.model.service.ContaService;
import org.openjfx.model.service.FinanceiroService;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TelaFinanceiroController implements Initializable, DataChangeListener {
    private FinanceiroService service;
    private CaixaMensal caixaAberto;

    @FXML
    private Label labelMesAno;
    @FXML
    private Label labelSaldoInicial;
    @FXML
    private Label labelSaldoAtual;
    @FXML
    private Label labelTotalAportes;
    @FXML
    private Label labelTotalDespesas;
    @FXML
    private TableView<ContaDTO> tableViewContas;
    @FXML
    private TableView<AporteDTO> tableViewAportes;
    @FXML
    private JFXTextField jfxTextFieldBuscarAporte;
    @FXML
    private JFXTextField jfxTextFieldBuscarCaixa;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataCadastro;
    @FXML
    private TableColumn<ContaDTO, String> tableColumnContaDescricao;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValor;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValorPago;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataVencimento;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataPagamento;

    @FXML
    private TableColumn<ContaDTO, ContaDTO> tableColumnPAGAR;

    @FXML
    private TableColumn<AporteDTO, String> tableColumnColaborador;
    @FXML
    private TableColumn<AporteDTO, Date> tableColumnData;
    @FXML
    private TableColumn<AporteDTO, String> tableColumnDescricao;
    @FXML
    private TableColumn<AporteDTO, Double> tableColumnAporte;
    @FXML
    private JFXButton jfxButtonFecharAbrirCaixa;

    @FXML
    private void fecharAbrirCaixa(){
        service.fecharCaixa(this.caixaAberto);
        //TODO ARRUMAR ISSO
        CaixaMensal caixaMensal = new CaixaMensal(this.caixaAberto.getMes()+1, this.caixaAberto.getAno(), 0D);
        service.abrirCaixa(caixaMensal);
        setCaixaAberto(caixaMensal);
        updateTableViewConta();
        updateTableViewAporte();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableConta();
        initializeTableAporte();
    }

    public void setFinanceiroService(FinanceiroService financeiroService) {
        this.service = financeiroService;
    }

    public void updateTableView() {

    }

    public void updateTableViewConta() {
        ContaService service = new ContaService();
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }
        List<ContaDTO> list = service.findByCaixa(this.caixaAberto);
        System.out.println(list);
        ObservableList<ContaDTO> obsListContas = FXCollections.observableArrayList(list);
        tableViewContas.setItems(obsListContas);
//        initEditButtons();
//        initRemoveButtons();
        initPagarButton();

    }

    public void updateTableViewAporte() {
        AporteService service = new AporteService();
        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }
        List<AporteDTO> list = service.findByCaixa(caixaAberto);
        System.out.println(list);
        ObservableList<AporteDTO> obsListAporte = FXCollections.observableArrayList(list);
        obsListAporte = FXCollections.observableArrayList(list);
        tableViewAportes.setItems(obsListAporte);
    }

    public void setCaixaAberto(CaixaMensal caixaMensal) {
        this.caixaAberto = caixaMensal;
        labelMesAno.setText(caixaMensal.getMes() + " / " + caixaMensal.getAno());
        labelSaldoInicial.setText(String.valueOf(caixaMensal.getSaldoInicial()));
        labelSaldoAtual.setText("in dev"); // TODO fazer método para calcular
        labelTotalAportes.setText("in dev"); // TODO fazer método para calcular no service
        labelTotalDespesas.setText("in dev"); // TODO adicionar o sum no get
    }

    private synchronized void initializeTableConta() {
        tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        tableColumnContaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnDataVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        tableColumnDataPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        tableColumnValorPago.setCellValueFactory(new PropertyValueFactory<>("valorPagamento"));
        Utils.formatTableColumnDouble(tableColumnValor, 2);
        Utils.formatTableColumnDouble(tableColumnValorPago, 2);
        Utils.formatTableColumnDate(tableColumnDataCadastro, "dd/MM/yyyy");
        Utils.formatTableColumnDate(tableColumnDataVencimento, "dd/MM/yyyy");
        Utils.formatTableColumnDate(tableColumnDataPagamento, "dd/MM/yyyy");
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
    }

    private synchronized void initializeTableAporte() {
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnAporte.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnColaborador.setCellValueFactory(new PropertyValueFactory<>("colaboradorNome"));
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
//        // Formatar a data usando o método do utils
        Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
//        // Formata o Salario usando o método do utils
        Utils.formatTableColumnDouble(tableColumnAporte, 2);
        Stage stage = (Stage) MainApp.getMainScene().getWindow();
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
                button.setDisable(false);
                if(obj.getDataPagamento()!=null){
                    if(obj.getValor() <= obj.getValorPagamento()){
                        button.setDisable(true);
                        button.setStyle("-fx-background-color: #7F9");
                        button.setText("Pago");
                    }else{
                        button.setDisable(false);
                        button.setStyle("-fx-background-color: #F55");
                        button.setText("Pagar restante ");
                    }

                }
                // seta a action do button
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event)));
            }

        });
    }

    private void createDialogForm(ContaDTO contaDTO, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/gui/PagarContaForm.fxml"));
            Pane pane = loader.load();
            // Controller
            PagarContaFormController controller = loader.getController();
            controller.setEntity(contaDTO);
            controller.setEntityPagamento(contaDTO);
            controller.setCaixaMensal(this.caixaAberto);
            controller.setServices(new ContaService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            controller.loadComboBox();
            controller.updateFormPagamentoData();
            // Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados de Pagamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(currentStage);
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
        updateTableViewAporte();
        updateTableViewConta();
    }

    @Override
    public void onLogin(Object logado) {

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
}