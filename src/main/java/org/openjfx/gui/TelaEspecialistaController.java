package org.openjfx.gui;

import javafx.fxml.Initializable;
import org.openjfx.gui.listener.DataChangeListener;
import org.openjfx.model.entities.Funcionario;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaEspecialistaController implements Initializable {
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

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        // add lista
    }
}
