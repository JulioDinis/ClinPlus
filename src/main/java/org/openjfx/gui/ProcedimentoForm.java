package org.openjfx.gui;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.PacienteService;
import org.openjfx.model.service.ProcedimentoService;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcedimentoForm {
    private Procedimento entity;
    private ProcedimentoService service;
    private ProcedimentoService procedimentoService;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();


    //FXML

    @FXML
    private JFXTextArea txtDescricao;
    @FXML
    private JFXTextField txtValor;
    @FXML
    private Label labelEspecialista;

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
            service.saveOrUpdate(entity);

            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            //           e.printStackTrace(); it is ok
       //     setErrosMensagens(e.getErrors());

        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }


    public Procedimento getEntity() {
        return entity;
    }

    public void setEntity(Procedimento entity) {
        this.entity = entity;
    }

    public ProcedimentoService getService() {
        return service;
    }

    public void setService(ProcedimentoService service, ProcedimentoService procedimentoService) {
        this.service = service;
        this.procedimentoService = procedimentoService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListener.add(listener);
    }

    public void updateFormData() {
        if (this.entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtDescricao.setText(entity.getDescricao());
        if (entity.getValor() == null) {
            txtValor.setText("");
        } else {
            txtValor.setText(entity.getValor().toString());
        }
        if(entity.getIdEspecialista() == null){
            labelEspecialista.setText("null");
        }
        else
            labelEspecialista.setText(entity.getIdEspecialista().toString());

    }

    private synchronized Procedimento getFormData() {
        Procedimento obj = new Procedimento();

        obj.setDescricao(txtDescricao.getText());
        obj.setIdEspecialista(entity.getIdEspecialista());
        obj.setValor(Double.parseDouble(txtValor.getText()));
        System.out.println(obj);
        return obj;
    }
    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }
}
