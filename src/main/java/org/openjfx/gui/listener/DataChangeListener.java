/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.gui.listener;

import org.openjfx.model.entities.Colaborador;

import java.util.function.Consumer;

/**
 * @author julio
 */
public interface DataChangeListener {

    void onDataChange();

    void onLogin(Colaborador p);

    void onLogout();

    <T> void onClickTela(String resource, Consumer<T> initialingAction);
}
