package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Data;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Data
public class TelaEspecialistaController implements Initializable {

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
    public void onJfxButtonHistoricoClick() {
        notifyDataChangeListeners("/org/openjfx/gui/FuncionarioList.fxml",
                (FuncionarioListController controller) -> {
                    controller.setFuncionarioService(new FuncionarioService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonAgendaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/TelaAgenda.fxml",
                (TelaAgendaController controller) -> {
                    controller.setAgendaService(new AgendaService());
                    controller.updateAgenda();
                });
    }

    @FXML
    public void onJfxButtonOrcamentoClick() {
        notifyDataChangeListeners("/org/openjfx/gui/TelaFinanceiro.fxml",
                (TelaFinanceiroController controller) -> {
                    controller.setFinanceiroService(new FinanceiroService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonLogoutClick() {
        System.out.println("<< --Logout()-- >>");
        notifyLogOut();
    }



    public void subscribeDataChangeListener(DataChangeListener ...listener) {
        System.out.println("ADD --> " + listener);
        Arrays.stream(listener).forEach(listen -> dataChangeListener.add(listen));
    }

    private <T> void notifyDataChangeListeners(String resource, Consumer<T> initialingAction) {
        System.out.println("<< Notificado >>");
        for (DataChangeListener listener : dataChangeListener) {
            listener.onClickTela(resource, initialingAction);
        }
    }
    private void notifyLogOut(){
        for (DataChangeListener listener : dataChangeListener) {
            listener.onLogout();
        }
    }

}
