package org.openjfx.gui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import org.jetbrains.annotations.NotNull;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.dto.ColaboradorDTO;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.Aporte;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.AporteService;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.ContaService;


import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PagarContaFormController extends ContaFormController implements Initializable {

    private ContaDTO entity = new ContaDTO();
    private ContaService service = new ContaService();
    private ColaboradorService colaboradorService = new ColaboradorService();
    private ObservableList<Colaborador> obsColaboradorEspecialidade;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();

    //    private ObservableList<Colaborador> obsColaboradorEspecialidade;
//    private ColaboradorService colaboradorService = new ColaboradorService();
//    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
//    private CaixaMensal caixaMensal;
    @FXML
    private JFXTextField jfxTextFieldValorPago;

    @FXML
    private JFXComboBox jfxComboBoxColaborador;
    //    @FXML
//    private JFXTextField jfxTextFieldDescricao;
//    @FXML
//    private JFXTextField jfxTextFieldObservacao;
    @FXML
    private DatePicker datePickerDataPagamento;

    //
//    @FXML
//    private Label labelCaixa;
//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        Locale ptBr = new Locale("pt", "BR");
        Locale.setDefault(ptBr);
        initializeNodes();
    }

    private void initializeNodes() {
        Utils.formatDatePicker(datePickerDataPagamento, "dd/MM/yyyy");
    }

    //
//    @FXML
//    public void onBtCancelAction(ActionEvent event) {
//        Utils.currentStage(event).close();
//    }
//
    @FXML
    @Override
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Entity was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            if(jfxComboBoxColaborador.getSelectionModel().getSelectedIndex() !=0){
                AporteService service = new AporteService();
                AporteDTO a = new AporteDTO();
                a.setCaixaMensal(entity.getCaixaMensal());
                a.setDescricao("Pagamento.....:      " + entity.getDescricao());
                a.setValor(Double.parseDouble(jfxTextFieldValorPago.getText()));
                a.setData(Utils.convertToDateViaSqlDate(datePickerDataPagamento.getValue()));
                a.setColaborador((Colaborador) jfxComboBoxColaborador.getSelectionModel().getSelectedItem());
                service.saveOrUpdate(a);
            }
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
//            e.printStackTrace(); it is ok
//            setErrosMensagens(e.getErrors());
        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private @NotNull ContaDTO getFormData() {
        ContaDTO contaDTO = new ContaDTO();
        if (entity.getIdConta() != null) {
            contaDTO = entity;
            contaDTO.setDataPagamento(Utils.convertToDateViaSqlDate(datePickerDataPagamento.getValue()));
            contaDTO.setValorPagamento(Double.parseDouble(jfxTextFieldValorPago.getText()));
        }
        return contaDTO;
    }

    //
//    public void setCaixaMensal(CaixaMensal caixaMensal) {
//        this.caixaMensal = caixaMensal;
//    }
//
    public void setEntityPagamento(ContaDTO entity) {
        this.entity = entity;
    }
//
//    public void setServices(ContaService contaService) {
//        this.service = contaService;
//    }

    public void updateFormPagamentoData() {
        System.out.println(entity);
        jfxTextFieldValorPago.setText(String.valueOf(entity.getValor() != null ? entity.getValor() : "0,00"));
        datePickerDataPagamento.setValue(entity.getDataVencimento().toLocalDate());
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListener.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }

    public void loadComboBox() {
        Colaborador c = new Colaborador();
        c.setNome("Retirada de Caixa");
        c.setEspecialidade("Caixa");
        obsColaboradorEspecialidade = FXCollections.observableArrayList();
        obsColaboradorEspecialidade.add(c);
        obsColaboradorEspecialidade.addAll(colaboradorService.findAllAtivos());
        jfxComboBoxColaborador.setItems(obsColaboradorEspecialidade);
        jfxComboBoxColaborador.getSelectionModel().select(0);
    }
}
