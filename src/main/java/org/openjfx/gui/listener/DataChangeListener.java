/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui.listener;

import org.openjfx.model.entities.Funcionario;
import org.openjfx.model.entities.Paciente;

import java.util.function.Consumer;

/**
 * @author julio
 */
public interface DataChangeListener {

    void onDataChange();

    void onLogin(Funcionario p);

    void onLogout();

    <T> void onClickTela(String resource, Consumer<T> initialingAction);
}
