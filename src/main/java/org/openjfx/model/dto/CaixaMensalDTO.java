package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjfx.model.entities.Aporte;
import org.openjfx.model.service.FinanceiroService;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class CaixaMensalDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer mes;
    private Integer ano;
    private Double saldoInicial;
    private Double saldoFinal;
    private Double saldoAtual;
    private String status;

    public CaixaMensalDTO(Integer mes, Integer ano, Double saldoInicial) {
        this.mes = mes;
        this.ano = ano;
        this.saldoInicial = saldoInicial;
        this.status = "Aberto";
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(Double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Double getSaldoAtual() {
        FinanceiroService service = new FinanceiroService();
        saldoAtual = service.buscaSaldoAtual(this);
        return saldoAtual;
    }

    public void setSaldoAtual(Double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
