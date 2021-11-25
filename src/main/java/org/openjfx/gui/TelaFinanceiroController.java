package org.openjfx.gui;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.openjfx.application.MainApp;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.ContaService;
import org.openjfx.model.service.FinanceiroService;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class TelaFinanceiroController implements Initializable {
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
    private TableColumn<ContaDTO, String> tableColumnDescricao;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValor;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataVencimento;
    @FXML
    private TableColumn<ContaDTO, Date> tableColumnDataPagamento;
    @FXML
    private TableColumn<ContaDTO, Double> tableColumnValorPagamento;
    @FXML
    private TableColumn<ContaDTO, String> tableColumnObservacao;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFinanceiroService(FinanceiroService financeiroService) {
        this.service = financeiroService;
    }

    public void updateTableView() {

    }
    public void updateTableViewConta(){
        ContaService service = new ContaService();


    }
    public void updateTableViewAporte(){

    }

    public void setCaixaAberto(CaixaMensal caixaMensal) {
        this.caixaAberto = caixaMensal;
        labelMesAno.setText(caixaMensal.getMes() + " / " + caixaMensal.getAno());
        labelSaldoInicial.setText(String.valueOf(caixaMensal.getSaldoInicial()));
        labelSaldoAtual.setText("in dev"); // TODO fazer método para calcular
        labelTotalAportes.setText("in dev"); // TODO fazer método para calcular no service
        labelTotalDespesas.setText("in dev"); // TODO adicionar o sum no get
    }

    private synchronized void initializeNodes() {
//        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
//        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
//        tableColumnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
//        tableColumnCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
//        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
//        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
//        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
//        tableColumnWhatsApp.setCellValueFactory(new PropertyValueFactory<>("whatsApp"));
//        // Formatar a data usando o método do utils
//        Utils.formatTableColumnDate(tableColumnDataNascimento, "dd/MM/yyyy");
//
////        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
//        Stage stage = (Stage) MainApp.getMainScene().getWindow();
//
//        tableViewPaciente.prefHeightProperty().bind(stage.heightProperty());

    }
}
