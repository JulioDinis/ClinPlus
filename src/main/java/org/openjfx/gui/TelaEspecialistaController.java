package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.gui.util.Alerts;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Paciente;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Data
public class TelaEspecialistaController implements Initializable, DataChangeListener {

    private List<DataChangeListener> dataChangeListener = new ArrayList<>();
    private Colaborador especialistaLogado;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private JFXButton jfxButtonHistorico;
    @FXML
    private JFXButton jfxButtonOrcamento;

    @FXML
    private JFXButton jfxButtonAgenda;
    @FXML
    private JFXButton jfxButtonProcedimento;
    @FXML
    private JFXButton jfxButtonLogout;


    @FXML
    public void onJfxButtonProcedimentoClick() {
        notifyDataChangeListeners("/org/openjfx/gui/ProcedimentoList.fxml",
                (ProcedimentoListController controller) -> {
                    controller.setProcedimentoService(new ProcedimentoService());
                    controller.setFuncionarioLogado(this.getEspecialistaLogado());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonAtendimentoClick() {
        notifyDataChangeListeners("/org/openjfx/gui/TelaAtendimento.fxml",
                (TelaAtendimentoController controller) -> {
                    controller.setServices(new ColaboradorService(), new PacienteService(), new ItensTratamentoService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonAgendaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/Agenda.fxml",
                (AgendaController controller) -> {
                    controller.setServices(new AgendaService(), new ColaboradorService());
                    controller.loadComboBox();
                    controller.setEspecialista(this.especialistaLogado);
                    controller.subscribeDataChangeListener(this);
                });
    }

    @FXML
    public void onJfxButtonOrcamentoClick(ActionEvent event) {
    //    createDialogForm("/org/openjfx/gui/OrcamentoList.fxml", Utils.currentStage(event));
    }

    @FXML
    public void onJfxButtonLogoutClick() {
        System.out.println("<< --Logout()-- >>");
        notifyLogOut();
    }




    public void subscribeDataChangeListener(DataChangeListener... listener) {
        System.out.println("ADD --> " + listener);
        Arrays.stream(listener).forEach(listen -> dataChangeListener.add(listen));
    }

    private <T> void notifyDataChangeListeners(String resource, Consumer<T> initialingAction) {
        System.out.println("<< Notificado >>");
        for (DataChangeListener listener : dataChangeListener) {
            listener.onClickTela(resource, initialingAction);
        }

    }

    private void notifyLogOut() {
        for (DataChangeListener listener : dataChangeListener) {
            listener.onLogout();
        }
    }

    @Override
    public void onDataChange() {

    }

    @Override
    public void onLogin(Object logado) {

    }

    @Override
    public void onCaixaAbertoChange(CaixaMensal caixaAberto) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {
        notifyDataChangeListeners(resource, initialingAction);
    }
}
