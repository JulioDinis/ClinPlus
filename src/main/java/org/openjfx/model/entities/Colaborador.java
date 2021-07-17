package org.openjfx.model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Colaborador extends Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idFuncionario;
    private String funcao;
    private String especialidade;
    private String senha;
    private Double salario;
    private Date dataContrato;
    private Date dataDesligamento;

    public Colaborador(Integer idPessoa, String nome, String cpf, String rg, Date dataNascimento, String sexo, String email, String logradouro, String cidade, String bairro, String cep, String uf, String telefone, boolean ativo, Integer idFuncionario) {
        super(idPessoa, nome, cpf, rg, dataNascimento, sexo, email, logradouro, cidade, bairro, cep, uf, telefone, ativo);
        this.idFuncionario = idFuncionario;
    }

    public Colaborador() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
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
        if (!(o instanceof Colaborador)) return false;
        if (!super.equals(o)) return false;
        Colaborador that = (Colaborador) o;
        return Objects.equals(getIdFuncionario(), that.getIdFuncionario()) && Objects.equals(getFuncao(), that.getFuncao()) && Objects.equals(getEspecialidade(), that.getEspecialidade()) && Objects.equals(getSenha(), that.getSenha()) && Objects.equals(getSalario(), that.getSalario()) && Objects.equals(getDataContrato(), that.getDataContrato()) && Objects.equals(getDataDesligamento(), that.getDataDesligamento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdFuncionario(), getFuncao(), getEspecialidade(), getSenha(), getSalario(), getDataContrato(), getDataDesligamento());
    }

    @Override
    public String toString() {
        return "Colaborador{" +
                "idFuncionario=" + idFuncionario +
                ", funcao='" + funcao + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", senha='" + senha + '\'' +
                ", salario=" + salario +
                ", dataContrato=" + dataContrato +
                ", dataDesligamento=" + dataDesligamento +
                '}';
    }
}
