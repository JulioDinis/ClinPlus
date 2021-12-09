package org.openjfx.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TiposPagamentoEnum {

    CARTAO("CARTAO"),
    TRANSFERENCIA("TRANSFERENCIA"),
    ESPECIE("ESPECIE"),
    CHEQUE("CHEQUE");

    private final String description;
}
