package org.openjfx.gui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
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

    public void setTipo(String accessibleText) {

        System.out.println("Quem chamou →→ " + accessibleText);

        /**
         * switch (buttonClicked.getAccessibleText()) {
         *                 case "Paciente": {
         *                     controller.setTipo(buttonClicked.getAccessibleText());
         *                     break;
         *                 }
         *                 case "Especialista":
         *                     controller.setTipo(buttonClicked.getAccessibleText());
         *                     break;
         *                 case "Procedimento":
         *                     controller.setTipo(buttonClicked.getAccessibleText());
         *                     break;
         *             }
         */

    }
}
