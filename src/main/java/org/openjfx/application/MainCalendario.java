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
import org.openjfx.gui.LoginFormController;
import org.openjfx.gui.MainAppViewController;
import org.openjfx.gui.TelaAtendenteController;
import org.openjfx.gui.TelaEspecialistaController;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.AtendenteService;
import org.openjfx.model.service.ColaboradorService;

import java.util.function.Consumer;

public class MainCalendario extends Application implements DataChangeListener {

    private static Scene mainScene;
    private static Stage stage;
    private Object logado;

    public Object getLogado() {
        return this.logado;
    }

    public void setFuncionarioLogado(Object logado) {
        this.logado = logado;
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

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
    public void onLogout() {
        System.out.println("Logout Solicitado");
        this.stage.close();
        this.logado = null;
    }
    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {
    }
}
