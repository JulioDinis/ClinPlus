package org.openjfx.gui.listener;

import org.openjfx.model.dto.AgendaDTO;

import java.util.function.Consumer;

public interface AgendaListener {

    <T> void atendimentoChange(String resource, Consumer<T> initialingAction, AgendaDTO agendaDTO);

}
