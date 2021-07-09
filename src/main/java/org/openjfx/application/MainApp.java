package org.openjfx.application;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.gui.*;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.service.FuncionarioService;

import java.util.function.Consumer;

public class MainApp extends Application implements DataChangeListener {


    private static Scene mainScene;
    private Funcionario funcionarioLogado;

    public Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }


    @Override
    public void start(Stage primaryStage) {
        try {

            Funcionario funcionario = new Funcionario();
            String caminhoDoFXML = "";
            createDialogForm(funcionario, "/org/openjfx/gui/LoginForm.fxml", primaryStage);

            if (this.getFuncionarioLogado() == null) {

                createDialogForm(funcionario, "/org/openjfx/gui/LoginForm.fxml", primaryStage);

            } else {

                Funcionario funcionarioLogado = this.getFuncionarioLogado();
                String funcao = funcionarioLogado.getFuncao();

                caminhoDoFXML = "/org/openjfx/gui/MainAppView.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDoFXML));
                ScrollPane scrollPane = loader.load();
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);

                mainScene = new Scene(scrollPane);
                primaryStage.setScene(mainScene);

                primaryStage.setTitle("ClinPlus - Painel Administrativo");
                MainAppViewController controller = loader.getController();
                controller.setFuncionarioLogado(this.getFuncionarioLogado());

                if (funcao.equals("Atendente")) {

                    System.out.println("###>>> Login Atendente <<<###");

                    controller.buttonAction("/org/openjfx/gui/TelaAtendente.fxml",
                            (TelaAtendenteController telaAtendenteController) -> {
//                              controller.setFuncionarioService(new FuncionarioService());
//                              controller.setFuncionarioLogado(this.funcionarioLogado);
//                              controller.updateTableView();
                                telaAtendenteController.subscribeDataChangeListener(controller);
                            });

                } else if (funcao.equals("Especialista")) {
                    System.out.println("###>>> Login Especialista <<<###");

                    controller.buttonAction("/org/openjfx/gui/TelaEspecialista.fxml",
                            (TelaEspecialistaController telaEspecialistaController) -> {

                            });

                } else {
//                    System.out.println("login Invalido");
//                    caminhoDoFXML = "/org/openjfx/gui/LoginForm.fxml";
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDoFXML));
//                    ScrollPane scrollPane = loader.load();
//                    scrollPane.setFitToHeight(true);
//                    scrollPane.setFitToWidth(true);
//                    mainScene = new Scene(scrollPane);
//                    primaryStage.setScene(mainScene);
//                    primaryStage.setTitle("ClinPlus");
//                    LoginFormController controller = loader.getController();
//                    controller.setServices(new FuncionarioService());
                }
                mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent ke) {
                        System.out.println("Key Pressed: " + ke.getCode());
                    }
                });
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createDialogForm(Funcionario funcionario, String absolutName, Stage parentStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();

            LoginFormController controller = loader.getController();
            controller.setFuncionario(funcionario);
            controller.setServices(new FuncionarioService());
            controller.subscribeDataChangeListener(this);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.setMaximized(false);
            dialogStage.initOwner(parentStage);
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
    }

    @Override
    public void onLogin(Funcionario funcionario) {
        System.out.println("###################### NOTIFICAÇÂO DE LOGIN #############");
        this.setFuncionarioLogado(funcionario);
    }

    @Override
    public void onLogout() {

    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {

    }
}
