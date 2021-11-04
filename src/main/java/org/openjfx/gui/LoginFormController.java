/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui;

import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.AtendenteService;
import org.openjfx.model.service.ColaboradorService;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class LoginFormController implements Initializable {

    private Colaborador entity;
    private AtendenteService atendenteService;
    private ColaboradorService colaboradorService;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private ObservableList<String> obsListSexo;
    private ObservableList<String> obsListUF;

    @FXML
    private JFXTextField txtCodigo;
    @FXML
    private JFXPasswordField txtSenha;

    @FXML
    private JFXButton jfxBtLogin;
    @FXML
    private JFXButton jfxBtCancelar;

    @FXML
    public void onBtLoginAction(ActionEvent event) {

        try {
            Atendente atendenteLogado = atendenteService.logar(getFormDataAtendente());
            Colaborador colaboradorLogado = colaboradorService.logar(getFormDataColaborador());
            if ((atendenteLogado == null) && (colaboradorLogado == null)) {
                ValidationException exception = new ValidationException("Validation error");
                exception.addError("login", "Credenciais recusadas");
                setErrosMensagens(exception.getErrors());
            } else if (atendenteLogado == null) {
                System.out.println("ESPECIALISTA LOGADO");
                notifyDataChangeListeners(colaboradorLogado);
            } else {
                System.out.println("ATENDENTE LOGADO");
                notifyDataChangeListeners(atendenteLogado);
            }
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            //           e.printStackTrace(); it is ok
            setErrosMensagens(e.getErrors());
        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

//    @FXML
//    public void onBtLoginAction(ActionEvent event) {
//        Colaborador logado;
//        Atendente atLogado;
//        if (entity == null) {
//            throw new IllegalStateException("Entity was null");
//        }
//        if (colaboradorService == null) {
//            throw new IllegalStateException("Entity was null");
//        }
//        try {
//            entity = getFormData();
//            atLogado = atendenteService.logar(entity);
//            if (atLogado == null) {
//                ValidationException exception = new ValidationException("Validation error");
//                exception.addError("login", "Credenciais recusadas");
//                setErrosMensagens(exception.getErrors());
//            } else {
//                notifyDataChangeListeners(atLogado);
//                Utils.currentStage(event).close();
//            }
//        } catch (ValidationException e) {
//            //           e.printStackTrace(); it is ok
//            setErrosMensagens(e.getErrors());
//        } catch (DbException e) {
//            e.printStackTrace();
//            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
//        }
//
//    }

    @FXML
    public void onBtCancelarAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    public void setFuncionario(Colaborador entity) {
        this.entity = entity;
    }

    public void setServices(ColaboradorService colaboradorService, AtendenteService atendenteService) {
        this.colaboradorService = colaboradorService;
        this.atendenteService = atendenteService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        System.out.println("ADD --> " + listener);
        dataChangeListener.add(listener);
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    private void setErrosMensagens(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        System.out.println("ERROS  ------------------------------->" + fields);
        // Com operador ternário.
        txtCodigo.setStyle(fields.contains("login") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtCodigo.setFocusColor(fields.contains("login") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtCodigo.setStyle(fields.contains("codigo") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtCodigo.setFocusColor(fields.contains("dodigo") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtSenha.setStyle(fields.contains("senha") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtSenha.setFocusColor(fields.contains("senha") ? Color.valueOf("red") : Color.LIGHTGRAY);

    }

    private synchronized Colaborador getFormDataColaborador() {
        Colaborador colaborador = new Colaborador();
        ValidationException exception = new ValidationException("Validation error");
        colaborador.setIdColaborador(Utils.tryParseToInt(txtCodigo.getText()));
        if (txtCodigo.getText() == null || txtCodigo.getText().isEmpty()) {
            colaborador.setIdColaborador(Integer.parseInt(txtCodigo.getText()));
        }
        if (txtSenha.getText() == null || txtSenha.getText().isEmpty()) {
            exception.addError("senha", "Digite a senha");
        } else {
            colaborador.setSenha(Hashing.sha256()
                    .hashString(txtSenha.getText(), StandardCharsets.UTF_8).toString());
        }
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        System.out.println(colaborador);
        return colaborador;
    }

    private synchronized Atendente getFormDataAtendente() {
        Atendente atendente = new Atendente();
        ValidationException exception = new ValidationException("Validation error");
        atendente.setIdAtendente(Utils.tryParseToInt(txtCodigo.getText()));
        if (txtCodigo.getText() == null || txtCodigo.getText().isEmpty()) {
            atendente.setIdAtendente(Integer.parseInt(txtCodigo.getText()));
        }
        if (txtSenha.getText() == null || txtSenha.getText().isEmpty()) {
            exception.addError("senha", "Digite a senha");
        } else {
            atendente.setSenha(Hashing.sha256()
                    .hashString(txtSenha.getText(), StandardCharsets.UTF_8).toString());
        }
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        System.out.println(atendente);
        return atendente;
    }

    private void notifyDataChangeListeners(Object colaborador) {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onLogin(colaborador);
        }
    }
}
