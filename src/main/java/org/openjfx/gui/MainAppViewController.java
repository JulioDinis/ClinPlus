package org.openjfx.gui;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.openjfx.application.MainApp;
import org.openjfx.application.ToolbarActionCallBack;
import org.openjfx.gui.util.Alerts;
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.service.FuncionarioService;
import org.openjfx.model.service.PacienteService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainAppViewController implements Initializable, ToolbarActionCallBack {
    private Funcionario funcionarioLogado;
    @FXML
    private MenuItem menuItemFuncionario;

    @FXML
    private MenuItem menuItemProcedimento;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private MenuItem menuItemPaciente;

    @FXML
    private Text textNomeDoUsuarioLogado;

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer menuLateral;

    @FXML
    public void onMenuItemPacienteAction() {
        buttonAction("/org/openjfx/gui/PacienteList.fxml", (PacienteListController controller) -> {
            controller.setPacienteService(new PacienteService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemFuncionarioAction() {
        buttonAction("/org/openjfx/gui/FuncionarioList.fxml", (FuncionarioListController controller) -> {
            controller.setFuncionarioService(new FuncionarioService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemProcedimentoAction() {
//        loadView("/org/openjfx/gui/ProcedimentoList.fxml", (ProcedimentoListController controller) -> {
//            controller.setProcedimentoService(new ProcedimentoService());
//            controller.updateTableView();
//        });
    }

    @FXML
    public void onMenuItemAboutAction() {
        buttonAction("/org/openjfx/gui/About.fxml", x -> {
        });
    }

    public BorderPane getBorderPanePrincipal() {
        return borderPanePrincipal;
    }

    public void setBorderPanePrincipal(BorderPane borderPanePrincipal) {
        this.borderPanePrincipal = borderPanePrincipal;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.carregarMenu(true);
        // TODO
    }

    private void carregarMenu(boolean expandido) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/gui/Toolbar.fxml"));
            VBox box = loader.load();
            ToolbarController controller = loader.getController();
            controller.setActionCallBack(this);

            // não remover
            menuLateral.setSidePane(box);

        } catch (IOException ex) {
            System.out.println("Deu merda");
            System.out.println(ex);
        }
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            System.out.println("botão clicaddo");
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (menuLateral.isOpened()) {
                menuLateral.close();
            } else {
                menuLateral.open();
            }
        });
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;

        if (this.funcionarioLogado != null)
            textNomeDoUsuarioLogado.setText(this.funcionarioLogado.getNome());
    }
    // Carrega a view - o synchronized garante que o carregamento não será interrompido no meio

    /**
     * função parametrizada
     *
     * @param <T>
     * @param absoluteName
     * @param initialingAction
     */
    public synchronized <T> void loadViewOk(String absoluteName, Consumer<T> initialingAction) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
            // Carrega o Scene Principal
            Scene mainScene = MainApp.getMainScene();
            // Pega o VBox da classe principal onde será inserido os node
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            // Guarda o node da barra de menu
            Node mainMenu = mainVbox.getChildren().get(0);
            Node tollBar = mainVbox.getChildren().get(1);

            mainVbox.getChildren().clear(); //limpa o VBox da Main
            mainVbox.getChildren().add(mainMenu); // Adiciona o node da Barra de Menu
            mainVbox.getChildren().add(tollBar); // Adiciona o node  da toolbar
            mainVbox.getChildren().addAll(newVBox.getChildren()); // Adiciona os node da tela about

            // executa a função que veio por parametro T
            T controller = loader.getController();
            initialingAction.accept(controller);

        } catch (Exception e) {
            System.out.println("Deu erro aqui");
            e.printStackTrace();
            Alerts.showAlert("IO Exeption", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    /**
     * função parametrizada
     *
     * @param <T>
     * @param absoluteName
     * @param initialingAction
     */
    @Override
    public synchronized <T> void buttonAction(String absoluteName, Consumer<T> initialingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
            Scene mainScene = MainApp.getMainScene();
            borderPanePrincipal.getCenter();
            VBox mainVbox = (VBox) borderPanePrincipal.getCenter();
            System.out.println(mainVbox);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().addAll(newVBox.getChildren()); // Adiciona os node da tela about
            T controller = loader.getController();
            initialingAction.accept(controller);
        } catch (Exception e) {
            System.out.println("Deu erro aqui");
            e.printStackTrace();
            Alerts.showAlert("IO Exeption", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


//    private synchronized void loadView2(String absoluteName) {
//        try {
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//            VBox newVBox = loader.load();
//            // Carrega o Scene Principal
//            Scene mainScene = Main.getMainScene();
//            // Pega o VBox da classe principal onde será inserido os node
//            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
//
//            // Guarda o node da barra de menu
//            Node mainMenu = mainVbox.getChildren().get(0);
//            mainVbox.getChildren().clear(); //limpa o VBox da Main
//            mainVbox.getChildren().add(mainMenu); // Adiciona o node da Barra de Menu
//            mainVbox.getChildren().addAll(newVBox.getChildren()); // Adiciona os node da tela about
//
//            DepartmentListController controller = loader.getController();
//            controller.setDepartmentService(new DepartmentService());
//            controller.updateTableView();
//
//        } catch (Exception e) {
//            Alerts.showAlert("IO Exeption", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
//        }
//
//    }
}
