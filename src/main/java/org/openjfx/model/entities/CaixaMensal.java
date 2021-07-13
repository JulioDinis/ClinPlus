package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaixaMensal implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer mes;
    private Integer ano;
    private Double saldoInicial;
    private Double saldoFinal;
    private Boolean status;
}
