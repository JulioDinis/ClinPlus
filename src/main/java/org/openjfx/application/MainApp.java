package org.openjfx.application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.gui.*;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.AtendenteService;
import org.openjfx.model.service.ColaboradorService;

import java.util.function.Consumer;

public class MainApp extends Application implements DataChangeListener {

    private static Scene mainScene;
    private static Stage stage;
    private Object logado;
    private CaixaMensal caixaAberto;

    public Object getLogado() {
        return this.logado;
    }

    public void setFuncionarioLogado(Object logado) {
        this.logado = logado;
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.login(this.stage);
        this.stage.getIcons().add(new Image("https://i.pinimg.com/474x/d0/b8/c9/d0b8c9efc8b5e3c582103a33c385dfb4.jpg"));

    }

    private void login(Stage primaryStage) {
        try {
            Colaborador colaborador = new Colaborador();
            String caminhoDoFXML = "";
            createDialogForm(colaborador, "/org/openjfx/gui/LoginForm.fxml", primaryStage);
            if (this.getLogado() == null) {
                createDialogForm(colaborador, "/org/openjfx/gui/LoginForm.fxml", primaryStage);
            } else {
                //    Colaborador colaboradorLogado = (Colaborador) this.getLogado();
                String funcao = "";
                if (this.getLogado() instanceof Colaborador) {
                    funcao = "Especialista";
                    System.out.println("ESPECIALISTA=> ");
                } else if (this.getLogado() instanceof Atendente) {
                    funcao = "Atendente";
                    System.out.println("ATENDENTE" + this.getLogado());
                } else {
                    System.out.println("ISSO --->>> " + this.getLogado().getClass());
                }
                caminhoDoFXML = "/org/openjfx/gui/MainAppView.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDoFXML));
                ScrollPane scrollPane = loader.load();
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);
                mainScene = new Scene(scrollPane);
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("ClinPlus - Painel Administrativo");
                MainAppViewController controller = loader.getController();
                controller.setLogado(this.getLogado());
                controller.setParent(this);
                controller.setCaixaAberto(this.caixaAberto);
                if (funcao.equals("Atendente")) {
                    System.out.println("###>>> Login Atendente <<<###");
                    controller.buttonAction("/org/openjfx/gui/TelaAtendente.fxml",
                            (TelaAtendenteController telaAtendenteController) -> {
                                telaAtendenteController.subscribeDataChangeListener(controller, this);
                                telaAtendenteController.setCaixaAberto(this.caixaAberto);
                            });
                } else if (funcao.equals("Especialista")) {
                    Colaborador especialista = (Colaborador) this.getLogado();
                    System.out.println("###>>> Login Especialista <<<###");
                    controller.buttonAction("/org/openjfx/gui/TelaEspecialista.fxml",
                            (TelaEspecialistaController telaEspecialistaController) -> {
                                telaEspecialistaController.setEspecialistaLogado(especialista);
                                telaEspecialistaController.subscribeDataChangeListener(controller);
                                telaEspecialistaController.subscribeDataChangeListener(this);
                            });
                } else {
                    System.out.println("INDEFINIDO");
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
            controller.setServices(new ColaboradorService(), new AtendenteService());
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
    public void onLogin(Object logado) {
        System.out.println("###################### NOTIFICAÇÃO DE LOGIN #############");
        this.setFuncionarioLogado(logado);
    }

    @Override
    public void onCaixaAbertoChange(CaixaMensal caixaAberto) {
        this.caixaAberto = caixaAberto;
    }

    @Override
    public void onLogout() {
        System.out.println("Logout Solicitado");
        this.stage.close();
        this.logado = null;
        login(this.stage);
    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {
    }
}
