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
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.FuncionarioService;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class LoginFormController implements Initializable {

    private Funcionario entity;
    private FuncionarioService service;
    private FuncionarioService funcionarioService;
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

        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Entity was null");
        }
        try {
            entity = getFormData();
            service.logar(entity);

            Funcionario logado = service.logar(entity);
            if (logado == null) {
                ValidationException exception = new ValidationException("Validation error");

                exception.addError("login", "Credenciais recusadas");
                setErrosMensagens(exception.getErrors());
            } else {
                notifyDataChangeListeners(logado);
                Utils.currentStage(event).close();
            }
//            service.saveOrUpdate(entity);
//            notifyDataChangeListeners();
//
        } catch (ValidationException e) {
            //           e.printStackTrace(); it is ok
            setErrosMensagens(e.getErrors());

        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void onBtCancelarAction(ActionEvent event) {

        Utils.currentStage(event).close();

    }

    public void setFuncionario(Funcionario entity) {
        this.entity = entity;
    }

    public void setServices(FuncionarioService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
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

    private synchronized Funcionario getFormData() {
        Funcionario obj = new Funcionario();

        ValidationException exception = new ValidationException("Validation error");

        obj.setIdFuncionario(Utils.tryParseToInt(txtCodigo.getText()));


        if (txtCodigo.getText() == null || txtCodigo.getText().isEmpty()) {
            obj.setIdFuncionario(Integer.parseInt(txtCodigo.getText()));
        }

        if (txtSenha.getText() == null || txtSenha.getText().isEmpty()) {
            exception.addError("senha", "Digite a senha");
        } else {
            obj.setSenha(Hashing.sha256()
                    .hashString(txtSenha.getText(), StandardCharsets.UTF_8).toString());
        }


        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        System.out.println(obj);
        return obj;

    }

    private void notifyDataChangeListeners(Funcionario funcionario) {
        System.out.println(funcionario);
        for (DataChangeListener listener : dataChangeListener) {
            listener.onLogin(funcionario);
        }
    }


}
