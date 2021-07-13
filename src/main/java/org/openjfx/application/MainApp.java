package org.openjfx.application;

import javafx.application.Application;
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
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.FuncionarioService;

import java.util.function.Consumer;

public class MainApp extends Application implements DataChangeListener {


    private static Scene mainScene;
    private static Stage stage;
    private Colaborador colaboradorLogado;

    public Colaborador getFuncionarioLogado() {
        return colaboradorLogado;
    }

    public void setFuncionarioLogado(Colaborador colaboradorLogado) {
        this.colaboradorLogado = colaboradorLogado;
    }


    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.login(this.stage);
    }

    private void login(Stage primaryStage) {
        try {
            Colaborador colaborador = new Colaborador();
            String caminhoDoFXML = "";
            createDialogForm(colaborador, "/org/openjfx/gui/LoginForm.fxml", primaryStage);

            if (this.getFuncionarioLogado() == null) {

                createDialogForm(colaborador, "/org/openjfx/gui/LoginForm.fxml", primaryStage);

            } else {

                Colaborador colaboradorLogado = this.getFuncionarioLogado();
                String funcao = colaboradorLogado.getFuncao();

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
                controller.setParent(this);


                if (funcao.equals("Atendente")) {

                    System.out.println("###>>> Login Atendente <<<###");

                    controller.buttonAction("/org/openjfx/gui/TelaAtendente.fxml",
                            (TelaAtendenteController telaAtendenteController) -> {
//                              controller.setFuncionarioService(new FuncionarioService());
//                              controller.setFuncionarioLogado(this.colaboradorLogado);
//                              controller.updateTableView();
                                telaAtendenteController.subscribeDataChangeListener(controller, this);

                            });

                } else if (funcao.equals("Especialista")) {
                    System.out.println("###>>> Login Especialista <<<###");
                    controller.buttonAction("/org/openjfx/gui/TelaEspecialista.fxml",
                            (TelaEspecialistaController telaEspecialistaController) -> {
                                telaEspecialistaController.setEspecialistaLogado(this.getFuncionarioLogado());
                                telaEspecialistaController.subscribeDataChangeListener(controller);
                                telaEspecialistaController.subscribeDataChangeListener(this);
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

    private void createDialogForm(Colaborador colaborador, String absolutName, Stage parentStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();

            LoginFormController controller = loader.getController();
            controller.setFuncionario(colaborador);
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
    public void onLogin(Colaborador colaborador) {
        System.out.println("###################### NOTIFICAÇÃO DE LOGIN #############");
        this.setFuncionarioLogado(colaborador);
    }

    @Override
    public void onLogout() {
        System.out.println("Logout Solicitado");
        this.stage.close();
        this.colaboradorLogado = null;
        login(this.stage);
    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {

    }
}
