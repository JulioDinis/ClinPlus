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
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.AporteService;
import org.openjfx.model.service.ColaboradorService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AporteFormController implements Initializable {

    private AporteDTO entity = new AporteDTO();
    private AporteService service = new AporteService();
    private ObservableList<Colaborador> obsColaboradorEspecialidade;
    private ColaboradorService colaboradorService = new ColaboradorService();
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private CaixaMensal caixaMensal;
    @FXML
    private JFXTextField jfxTextFieldValor;
    @FXML
    private JFXComboBox jfxComboBoxEspecialista;
    @FXML
    private JFXTextField jfxTextFieldDescricao;
    @FXML
    private DatePicker datePickerDataAporte;

    @FXML
    private Label labelCaixa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Utils.formatDatePicker(datePickerDataAporte, "dd/MM/yyyy");
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

    private @NotNull AporteDTO getFormData() {
        AporteDTO aporteDTO = new AporteDTO();
        if (entity.getId() != null) {
            aporteDTO.setId(entity.getId());
        }
        aporteDTO.setData(Utils.convertToDateViaSqlDate(datePickerDataAporte.getValue()));
        aporteDTO.setCaixaMensal(caixaMensal);
        aporteDTO.setColaborador((Colaborador) jfxComboBoxEspecialista.getSelectionModel().getSelectedItem());
        aporteDTO.setDescricao(jfxTextFieldDescricao.getText());
        aporteDTO.setValor(Double.parseDouble(jfxTextFieldValor.getText()));
        return aporteDTO;
    }

    public void setCaixaMensal(CaixaMensal caixaMensal) {
        this.caixaMensal = caixaMensal;
    }

    public void setEntity(AporteDTO entity) {
        this.entity = entity;
    }

    public void setServices(AporteService aporteService) {
        this.service = aporteService;
    }

    public void loadComboBox() {
        obsColaboradorEspecialidade = FXCollections.observableList(colaboradorService.findAllAtivos());
        jfxComboBoxEspecialista.setItems(obsColaboradorEspecialidade);
    }

    public void updateFormData() {
        labelCaixa.setText(caixaMensal.getMes() + " / " + caixaMensal.getAno());
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        jfxTextFieldDescricao.setText(entity.getDescricao() != null ? entity.getDescricao() : "");
        jfxTextFieldValor.setText(String.valueOf(entity.getValor() != null ? entity.getValor() : ""));
        Locale.setDefault(Locale.US);
        if (entity.getData() != null) {
            datePickerDataAporte.setValue(entity.getData().toLocalDate());
        }
        if (entity.getColaborador() == null) {
            jfxComboBoxEspecialista.getSelectionModel().selectFirst();
        } else {
            jfxComboBoxEspecialista.setValue(entity.getColaborador());
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
