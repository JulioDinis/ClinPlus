package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TelaAtendenteController implements Initializable, DataChangeListener {

    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */


    @FXML
    private JFXButton jfxButtonPaciente;
    @FXML
    private JFXButton jfxButtonEspecialista;

    @FXML
    private JFXButton jfxButtonAgenda;
    @FXML
    private JFXButton jfxButtonFinanceiro;
    @FXML
    private JFXButton jfxButtonLogout;
    private Atendente atendenteLogado;
    private CaixaMensal caixaAberto;


    @FXML
    public void onJfxButtonPacienteClick() {
        notifyDataChangeListeners("/org/openjfx/gui/PacienteList.fxml",
                (PacienteListController controller) -> {
                    controller.setPacienteService(new PacienteService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonEspecialistaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/FuncionarioList.fxml",
                (ColaboradorListController controller) -> {
                    controller.setFuncionarioService(new ColaboradorService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonAgendaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/Agenda.fxml",
                (AgendaController controller) -> {
                    controller.setServices(new AgendaService(), new ColaboradorService());
                    controller.subscribeDataChangeListener(this);
                    controller.loadComboBox();
                });
    }

    @FXML
    public void onJfxButtonFinanceiroClick() {
        notifyDataChangeListeners("/org/openjfx/gui/TelaFinanceiro.fxml",
                (TelaFinanceiroController controller) -> {
                    controller.setFinanceiroService(new FinanceiroService());
//                    controller.updateTableView();
                    controller.setCaixaAberto(this.caixaAberto);
                    controller.updateTableViewConta();
                    controller.updateTableViewAporte();
                });
    }
    @FXML
    public void onJfxButtonAporteClick() {
        notifyDataChangeListeners("/org/openjfx/gui/AporteList.fxml",
                (AporteListController controller) -> {
                    controller.setAporteDTOService(new AporteService());
                    controller.updateTableView();
                    controller.setCaixaAberto(this.caixaAberto);
                });
    }
    @FXML
    public void onJfxButtonContaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/ContaList.fxml",
                (ContaListController controller) -> {
                    controller.setContaService(new ContaService());
                    controller.updateTableView();
                    controller.setCaixaAberto(this.caixaAberto);
                });
    }

    @FXML
    public void onJfxButtonLogoutClick() {
        this.notifyLogOut();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

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

    }

    public void subscribeDataChangeListener(DataChangeListener... listener) {
        System.out.println("ADD --> " + listener);
        Arrays.stream(listener).forEach(listen -> dataChangeListener.add(listen));
    }

    private <T> void notifyDataChangeListeners(String resource, Consumer<T> initialingAction) {
        System.out.println("<< Notificado >>" + resource);
        for (DataChangeListener listener : dataChangeListener) {
            listener.onClickTela(resource, initialingAction);
        }
    }

    private void notifyLogOut() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onLogout();
        }
    }

    public void setAtendenteLogado(Atendente atendenteLogado) {
        this.atendenteLogado = atendenteLogado;
    }

    public void setCaixaAberto(CaixaMensal caixaAberto) {
        this.caixaAberto=caixaAberto;
    }
}
