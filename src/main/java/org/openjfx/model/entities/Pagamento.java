package org.openjfx.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.enums.TiposPagamentoEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pagamento implements Serializable {

    private Tratamento tratamento;
    private Integer numeroParcela;
    private Double valor;
    private Date dataPagamento;
    private String formaPagamento;

}
