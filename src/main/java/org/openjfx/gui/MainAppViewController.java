package org.openjfx.gui;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.openjfx.application.ToolbarActionCallBack;
import org.openjfx.gui.listener.AgendaListener;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Pessoa;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Getter
@Setter
public class MainAppViewController implements Initializable, ToolbarActionCallBack, DataChangeListener, AgendaListener {
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

        System.out.println(event.toString());
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
        System.out.println("LOGO -> " + logado.getClass());
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
        } else if (this.logado instanceof Colaborador) {
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

    /**
     * função parametrizada
     *
     * @param <T>              Indica o tipo do controlador
     * @param absoluteName     Caminho do fxml a ser carregado
     * @param initialingAction Controlador do fxml passado
     */
    @Override
    public synchronized <T> void buttonAction(String absoluteName, Consumer<T> initialingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
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
        System.out.println("*********************** TELA ALTERADA ******************************");
        updateCaixaAberto();
        buttonAction(resource, initialingAction);
    }

    private void updateCaixaAberto() {
        FinanceiroService service = new FinanceiroService();
        setCaixaAberto(service.buscaCaixaAberto().get(0));
    }

    public void setCaixaAberto(CaixaMensal caixaAberto) {
        this.caixaAberto = caixaAberto;
    }

    @Override
    public <T> void atendimentoChange(String resource, Consumer<T> initialingAction, AgendaDTO agendaDTO) {
        System.out.println("############## ATENDER PACIENTE ##################");
    }
}
