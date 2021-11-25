package org.openjfx.gui;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.ContaService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ContaFormController implements Initializable {

    private ContaDTO entity = new ContaDTO();
    private ContaService service = new ContaService();
    private ObservableList<Colaborador> obsColaboradorEspecialidade;
    private ColaboradorService colaboradorService = new ColaboradorService();
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private CaixaMensal caixaMensal;
    @FXML
    private JFXTextField jfxTextFieldValor;
    @FXML
    private JFXTextField jfxTextFieldDescricao;
    @FXML
    private JFXTextField jfxTextFieldObservacao;
    @FXML
    private DatePicker datePickerDataVenciemento;

    @FXML
    private Label labelCaixa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Utils.formatDatePicker(datePickerDataVenciemento, "dd/MM/yyyy");
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Entity was null");
        }
        try {
            entity = getFormData();
            System.out.println("oq que está indo" + entity);
            service.saveOrUpdate(entity);
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
            contaDTO.setIdConta(entity.getIdConta());
        }
        contaDTO.setDataCadastro(Utils.convertToDateViaSqlDate(datePickerDataVenciemento.getValue()));
        contaDTO.setCaixaMensal(caixaMensal);
        contaDTO.setDescricao(jfxTextFieldDescricao.getText());
        contaDTO.setValor(Double.parseDouble(jfxTextFieldValor.getText()));
        return contaDTO;
    }

    public void setCaixaMensal(CaixaMensal caixaMensal) {
        this.caixaMensal = caixaMensal;
    }

    public void setEntity(ContaDTO entity) {
        this.entity = entity;
    }

    public void setServices(ContaService contaService) {
        this.service = contaService;
    }


    public void updateFormData() {
        labelCaixa.setText(caixaMensal.getMes() + " / " + caixaMensal.getAno());
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        jfxTextFieldDescricao.setText(entity.getDescricao() != null ? entity.getDescricao() : "");
        jfxTextFieldValor.setText(String.valueOf(entity.getValor() != null ? entity.getValor() : ""));
        Locale.setDefault(Locale.US);
        if (entity.getDataCadastro() != null) {
            ;
        }
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListener.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }
}
