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
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.ProcedimentoService;

import java.util.ArrayList;
import java.util.List;

public class ProcedimentoFormController {
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
    private Colaborador colaborador;
    @FXML
    private Label labelCodigo;

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
        labelCodigo.setText(String.valueOf((entity.getIdProcedimento()==null)?"Novo":entity.getIdProcedimento()));
        txtDescricao.setText(entity.getDescricao());
        if (entity.getValor() == null) {
            txtValor.setText("");
        } else {
            txtValor.setText(entity.getValor().toString());
        }
        if (entity.getColaborador().getIdColaborador() == null) {
            labelEspecialista.setText("null");
        } else
            labelEspecialista.setText(this.colaborador.getNome() + " | " + this.colaborador.getEspecialidade());

    }

    private synchronized Procedimento getFormData() {
        Procedimento procedimento = new Procedimento();

        Integer id = null;
        try{
            id = Integer.parseInt(labelCodigo.getText());
        }catch (RuntimeException ex){
            System.out.println("\n+adicionando\n");
        }

        procedimento.setIdProcedimento(id);
        procedimento.setDescricao(txtDescricao.getText());
        procedimento.setColaborador(entity.getColaborador());
        procedimento.setValor(Double.parseDouble(txtValor.getText()));
        System.out.println(procedimento);
        return procedimento;
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }

    public void setColaborador(Colaborador colaboradorLogado) {
        this.colaborador = colaboradorLogado;
    }
}
