package org.openjfx.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    CANCELADO("CANCELADO"),
    AUSENTE("AUSENTE"),
    REAGENDADO("REAGENDADO"),
    ATENDENDO("ATENDENDO"),
    ATENDIDO("ATENDIDO");

    private final String description;
}
