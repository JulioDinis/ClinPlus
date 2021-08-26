package org.openjfx.model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Atendente extends Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idAtendente;
    private String senha;
    private Double salario;
    private Date dataContrato;
    private Date dataDesligamento;

    public Atendente(Integer idPessoa, String nome, String cpf, String rg, Date dataNascimento, String sexo, String email, String logradouro, String cidade, String bairro, String cep, String uf, String telefone, boolean ativo, Integer idAtendente) {
        super(idPessoa, nome, cpf, rg, dataNascimento, sexo, email, logradouro, cidade, bairro, cep, uf, telefone, ativo);
        this.idAtendente = idAtendente;
    }

    public Atendente() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdAtendente() {
        return idAtendente;
    }

    public void setIdAtendente(Integer idAtendente) {
        this.idAtendente = idAtendente;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Date getDataContrato() {
        return dataContrato;
    }

    public void setDataContrato(Date dataContrato) {
        this.dataContrato = dataContrato;
    }

    public Date getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(Date dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atendente)) return false;
        if (!super.equals(o)) return false;
        Atendente that = (Atendente) o;
        return Objects.equals(getIdAtendente(), that.getIdAtendente()) && Objects.equals(getSenha(), that.getSenha()) && Objects.equals(getSalario(), that.getSalario()) && Objects.equals(getDataContrato(), that.getDataContrato()) && Objects.equals(getDataDesligamento(), that.getDataDesligamento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdAtendente(), getSenha(), getSalario(), getDataContrato(), getDataDesligamento());
    }

    @Override
    public String toString() {
        return "Colaborador{" +
                "idFuncionario=" + idAtendente +
                ", senha='" + senha + '\'' +
                ", salario=" + salario +
                ", dataContrato=" + dataContrato +
                ", dataDesligamento=" + dataDesligamento +
                '}';
    }
}
