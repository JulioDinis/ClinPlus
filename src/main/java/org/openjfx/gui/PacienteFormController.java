/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.openjfx.db.DbException;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Constraints;
import org.openjfx.gui.util.Utils;
import org.openjfx.gui.util.ValidaCPF;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.exeption.ValidationException;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author julio
 */
public class PacienteFormController implements Initializable {

    private Paciente entity;
    private PacienteService service;
    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private ObservableList<String> obsListSexo;
    private ObservableList<String> obsListUF;

    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXTextField txtCpf;
    @FXML
    private JFXTextField txtRg;
    @FXML
    private DatePicker dpDataNascimento;
    @FXML
    private JFXComboBox<String> comboBoxSexo;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtLogradouro;
    @FXML
    private JFXTextField txtCidade;
    @FXML
    private JFXTextField txtBairro;
    @FXML
    private JFXTextField txtCep;
    @FXML
    private JFXComboBox<String> comboBoxUf;
    @FXML
    private JFXTextField txtTelefone;
    @FXML
    private JFXTextField txtWhatsApp;
    @FXML
    private Label labelCodigo;


    @FXML
    private JFXButton btSalve;
    @FXML
    private JFXButton btCancel;

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
            setErrosMensagens(e.getErrors());

        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error Saving object", null, e.getMessage(), AlertType.ERROR);
        }

    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    public void setPaciente(Paciente entity) {
        this.entity = entity;
    }

    public void setServices(PacienteService service) {
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
        initializeNodes();

    }

    private void initializeNodes() {
        Constraints.setTextFieldMaxLength(txtNome, 70);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        labelCodigo.setText(String.valueOf((entity.getIdPaciente() == null) ? "Novo" : entity.getIdPaciente()));
        txtNome.setText(entity.getNome());
        txtCpf.setText(entity.getCpf());
        txtRg.setText(entity.getRg());
        Locale.setDefault(Locale.US);
        if (entity.getDataNascimento() != null) {
            dpDataNascimento.setValue(
                    LocalDate.ofInstant(
                            entity.getDataNascimento().toInstant(),
                            ZoneId.systemDefault()
                    )
            );
        }
        if (entity.getSexo() == null) {
            comboBoxSexo.getSelectionModel().selectFirst();
        } else {
            comboBoxSexo.setValue(entity.getSexo());
        }
        txtEmail.setText(entity.getEmail());
        txtLogradouro.setText(entity.getLogradouro());
        txtCidade.setText(entity.getCidade());
        txtBairro.setText(entity.getBairro());
        txtCep.setText(entity.getCep());
        if (entity.getUf() == null) {
            comboBoxUf.getSelectionModel().selectFirst();
        } else {
            comboBoxUf.setValue(entity.getUf());
        }
        txtTelefone.setText(entity.getTelefone());
        txtWhatsApp.setText(entity.getWhatsApp());
    }

    private synchronized Paciente getFormData() {
        Paciente paciente = new Paciente();

        ValidationException exception = new ValidationException("Validation error");

        paciente.setIdPaciente(Utils.tryParseToInt(labelCodigo.getText()));

        if (isValido(txtNome, "nome", exception)) {
            paciente.setNome(txtNome.getText());
        }

        if (ValidaCPF.isCPF((txtCpf.getText().replace(".", "")).replace("-", ""))) {
            if (isValido(txtCpf, "cpf", exception)) {
                paciente.setCpf(ValidaCPF.imprimeCPF(txtCpf.getText()));
            }
        } else {
            exception.addError("cpf", "O campo não pode ficar vazio");
        }
        if (isValido(txtRg, "rg", exception))
            paciente.setRg(txtRg.getText());

        if (dpDataNascimento.getValue() == null) {
            exception.addError("dataNascimento", "Seleciona a data de nascimento");
        } else {
            // data Piker Pegando Valor
            Instant instant = Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
            paciente.setDataNascimento(Date.from(instant));
        }
        //sexo
        if (comboBoxSexo.getSelectionModel().isEmpty()) {
            exception.addError("sexo", "Selecione uma das opções");
        } else {
            paciente.setSexo(comboBoxSexo.getSelectionModel().getSelectedItem());
        }
        if (isValido(txtEmail, "email", exception)) {
            paciente.setEmail(txtEmail.getText());
        }
        if (isValido(txtLogradouro, "logradouro", exception)) {
            paciente.setLogradouro(txtLogradouro.getText());
        }

        if (isValido(txtCidade, "cidade", exception)) {
            paciente.setCidade(txtCidade.getText());
        }

        if (isValido(txtBairro, "bairro", exception))
            paciente.setBairro(txtBairro.getText());

        if (isValido(txtCep, "cep", exception))
            paciente.setCep(txtCep.getText());

        if (comboBoxUf.getSelectionModel().isEmpty()) {
            exception.addError("uf", "Selecione uma das opções");
        } else {
            String uf = comboBoxUf.getSelectionModel().getSelectedItem();
            paciente.setUf(uf);
        }
        if (isValido(txtTelefone, "telefone", exception))
            paciente.setTelefone(txtTelefone.getText());

        if (isValido(txtWhatsApp, "whatsApp", exception))
            paciente.setWhatsApp(txtWhatsApp.getText());


        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        System.out.println(paciente);
        return paciente;

    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onDataChange();
        }
    }

    public void loadComboBox() {
        obsListSexo = FXCollections.observableList(Utils.getSexo());
        obsListUF = FXCollections.observableList(Utils.getSiglaEstados());
        comboBoxSexo.setItems(obsListSexo);
        comboBoxUf.setItems(obsListUF);
    }

    private Boolean isValido(JFXTextField textField, String key, ValidationException exception) {
        Boolean valido = true;
        if (textField.getText() == null) {
            valido = false;
        } else if (textField.getText().trim().equals(""))
            valido = false;
        if (valido) {
            System.out.println("Componente - " + key + " =>" + valido);
        } else
            exception.addError(key, "O campo não pode ficar vazio");

        return valido;

    }

    private void setErrosMensagens(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        System.out.println("ERROS  ------------------------------->" + fields);

        if (fields.contains("nome")) {
            txtNome.setStyle("-fx-border-color: red");
            txtNome.setFocusColor(fields.contains("nome") ? Color.valueOf("red") : Color.LIGHTGRAY);
        } else {
            txtNome.setStyle("-fx-border-color: light gray");
        }
        txtNome.setFocusColor(fields.contains("nome") ? Color.valueOf("red") : Color.LIGHTGRAY);

        if (fields.contains("cpf")) {
            txtCpf.setStyle("-fx-border-color: red");
        } else {
            txtCpf.setStyle("-fx-border-color: light gray");
        }
        txtCpf.setFocusColor(fields.contains("cpf") ? Color.valueOf("red") : Color.LIGHTGRAY);

        // Com operador ternário.
        txtRg.setStyle(fields.contains("rg") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtRg.setFocusColor(fields.contains("rg") ? Color.valueOf("red") : Color.LIGHTGRAY);
        dpDataNascimento.setStyle(fields.contains("dataNascimento") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        comboBoxSexo.setStyle(fields.contains("sexo") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtEmail.setStyle(fields.contains("email") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtEmail.setFocusColor(fields.contains("email") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtLogradouro.setFocusColor(fields.contains("logradouro") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtLogradouro.setStyle(fields.contains("logradouro") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtCidade.setFocusColor(fields.contains("cidade") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtCidade.setStyle(fields.contains("cidade") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtBairro.setFocusColor(fields.contains("bairro") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtBairro.setStyle(fields.contains("bairro") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtCep.setFocusColor(fields.contains("cep") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtCep.setStyle(fields.contains("cep") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        comboBoxUf.setStyle(fields.contains("uf") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtTelefone.setFocusColor(fields.contains("telefone") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtTelefone.setStyle(fields.contains("telefone") ? "-fx-border-color: red" : "-fx-border-color: light gray");
        txtWhatsApp.setFocusColor(fields.contains("whatsApp") ? Color.valueOf("red") : Color.LIGHTGRAY);
        txtWhatsApp.setStyle(fields.contains("whatsApp") ? "-fx-border-color: red" : "-fx-border-color: light gray");

    }
}
