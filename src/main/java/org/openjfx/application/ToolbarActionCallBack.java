package org.openjfx.application;

import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

public interface ToolbarActionCallBack {
    public <T> void buttonAction(String absoluteName, Consumer<T> initialingAction);
}
