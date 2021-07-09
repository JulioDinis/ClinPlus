package org.openjfx.gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.service.FuncionarioService;
import org.openjfx.model.service.PacienteService;

import java.net.URL;
import java.util.ArrayList;
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
                (FuncionarioListController controller) -> {
                    controller.setFuncionarioService(new FuncionarioService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonAgendaClick() {
        notifyDataChangeListeners("/org/openjfx/gui/FuncionarioList.fxml",
                (FuncionarioListController controller) -> {
                    controller.setFuncionarioService(new FuncionarioService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonFinanceiroClick() {
        notifyDataChangeListeners("/org/openjfx/gui/FuncionarioList.fxml",
                (FuncionarioListController controller) -> {
                    controller.setFuncionarioService(new FuncionarioService());
                    controller.updateTableView();
                });
    }

    @FXML
    public void onJfxButtonLogoutClick() {
        notifyDataChangeListeners("/org/openjfx/gui/FuncionarioList.fxml",
                (FuncionarioListController controller) -> {
                    controller.setFuncionarioService(new FuncionarioService());
                    controller.updateTableView();
                });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void onDataChange() {

    }

    @Override
    public void onLogin(Funcionario p) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public <T> void onClickTela(String resource, Consumer<T> initialingAction) {

    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        System.out.println("ADD --> " + listener);
        dataChangeListener.add(listener);
    }

    private <T> void notifyDataChangeListeners(String resource, Consumer<T> initialingAction) {
        System.out.println("Solicitado ==>" + resource);
        for (DataChangeListener listener : dataChangeListener) {
            listener.onClickTela(resource, initialingAction);
        }
    }


}
