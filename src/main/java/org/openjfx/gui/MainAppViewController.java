package org.openjfx.gui;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.openjfx.application.MainApp;
import org.openjfx.application.ToolbarActionCallBack;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Pessoa;
import org.openjfx.model.service.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Getter
@Setter
public class MainAppViewController implements Initializable, ToolbarActionCallBack, DataChangeListener {
    private Object logado;

    private Colaborador colaboradorLogado;
    private DataChangeListener parent;
    @FXML
    private MenuItem menuItemEspecialista;
    @FXML
    private MenuItem menuItemAtendente;
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
    private CaixaMensal caixaAberto;

    @FXML
    public void onMenuItemPacienteAction() {
        buttonAction("/org/openjfx/gui/PacienteList.fxml", (PacienteListController controller) -> {
            controller.setPacienteService(new PacienteService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemEspecialistaAction() {
        buttonAction("/org/openjfx/gui/FuncionarioList.fxml", (ColaboradorListController controller) -> {
            controller.setFuncionarioService(new ColaboradorService());
            controller.updateTableView();
        });
    }
    @FXML
    public void onMenuItemAtendenteAction() {
        buttonAction("/org/openjfx/gui/AtendenteList.fxml", (AtendenteListController controller) -> {
            controller.setFuncionarioService(new AtendenteService());
            controller.updateTableView();
        });
    }
    @FXML
    public void onMenuItemProcedimentoAction() {
        buttonAction("/org/openjfx/gui/ProcedimentoList.fxml", (ProcedimentoListController controller) -> {
            controller.setProcedimentoService(new ProcedimentoService());
            controller.setFuncionarioLogado(getColaboradorLogado());
            controller.updateTableView();

        });
    }

    @FXML
    public void onMenuItemOrcamentoAction(ActionEvent event) {

        buttonAction("/org/openjfx/gui/OrcamentoList.fxml", (OrcamentoListController controller) -> {
//            controller.setProcedimentoService(new ProcedimentoService());
//            controller.setFuncionarioLogado(this.colaboradorLogado);
//            controller.updateList();
//            controller.updateTableView();

        });
    }

    @FXML
    public void onMenuItemAboutAction() {
        buttonAction("/org/openjfx/gui/About.fxml", x -> {
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //this.carregarMenu(true);

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

    public void setLogado(Object colaboradorLogado) {
        this.logado = colaboradorLogado;
        if (this.logado != null) {
            Pessoa p = (Pessoa) this.logado;
            textNomeDoUsuarioLogado.setText(p.getNome());

        }
    }

    public void setParent(DataChangeListener parent) {
        this.parent = parent;
    }

    @FXML
    public void logout() {
        this.logado = null;
    }

    @FXML
    public void onLogoClick() {
        System.out.println("LOGO -> "+logado.getClass());
        if (this.logado instanceof Atendente) {
            System.out.println("ATENDENTE LOGADO!");
            Atendente atendenteLogado = (Atendente) this.getLogado();
            buttonAction("/org/openjfx/gui/TelaAtendente.fxml",
                    (TelaAtendenteController controller) -> {
                        controller.setAtendenteLogado(atendenteLogado);
                        controller.setCaixaAberto(this.caixaAberto);
                        controller.subscribeDataChangeListener(this);
                        controller.subscribeDataChangeListener(this.parent);
                    });
        } else if(this.logado instanceof Colaborador) {
            System.out.println("COLABORADOR LOGADO!!");
            Colaborador colaborador = (Colaborador) this.getLogado();
            this.setColaboradorLogado(colaborador);
            buttonAction("/org/openjfx/gui/TelaEspecialista.fxml",
                    (TelaEspecialistaController controller) -> {
                        controller.setEspecialistaLogado(getColaboradorLogado());
                        controller.subscribeDataChangeListener(this);
                        controller.subscribeDataChangeListener(this.parent);
                    });
        }
    }

    private Object getLogado() {
        return this.logado;
    }
    // Carrega a view - o synchronized garante que o carregamento não será interrompido no meio

    private CaixaMensal getCaixaAberto(){
        System.out.println("CAIXA ABERTO -->> " +this.caixaAberto);
        return this.caixaAberto;
    }

    /**
     * função parametrizada
     *
     * @param <T>
     * @param absoluteName
     * @param initialingAction
     */
    public synchronized <T> void loadView(String absoluteName, Consumer<T> initialingAction) {

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
            mainVbox.getChildren().clear();
            mainVbox.getChildren().addAll(newVBox.getChildren()); // Adiciona os node da tela about
            T controller = loader.getController();
            initialingAction.accept(controller);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exeption", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Listeners
    @Override
    public void onDataChange() {
    }
    @Override
    public void onLogin(Object obj) {
    }

    @Override
    public void onCaixaAbertoChange(CaixaMensal caixaAberto) {

    }

    @Override
    public void onLogout() {
    }
    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {
        // Arrumar para funcionar com qualquer tela (USAR T)
        System.out.println("*********************** TELA ALTERADA ******************************");
        buttonAction(resource, initialingAction);
    }
    private void createDialogForm(String absolutName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
            Pane pane = loader.load();
            OrcamentoListController controller = loader.getController();
            controller.setServices(null, new ProcedimentoService(), new TratamentoService());
            controller.setFuncionarioLogado(getColaboradorLogado());

            controller.updateTableView();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Procedimento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (Exception e) {
            System.out.println("ERRO EM -> " + getClass());
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Erro Loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    public void setCaixaAberto(CaixaMensal caixaAberto) {
        this.caixaAberto=caixaAberto;
    }
}
