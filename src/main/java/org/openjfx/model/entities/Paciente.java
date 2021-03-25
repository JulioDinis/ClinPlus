package org.openjfx.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Paciente extends Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPaciente;
    private String whatsApp;
    private Date dataCadastro;

    public Paciente(Integer idPaciente, String whatsApp, Integer idPessoa, String nome, String cpf, String rg, Date dataNascimento, String sexo, String email, Date dataCadastro, String logradouro, String cidade, String bairro, String cep, String uf, String telefone, boolean ativo) {
        super(idPessoa, nome, cpf, rg, dataNascimento, sexo, email, logradouro, cidade, bairro, cep, uf, telefone, ativo);
        this.idPaciente = idPaciente;
        this.whatsApp = whatsApp;
        this.dataCadastro = dataCadastro;
    }

    public Paciente(Integer idPaciente, String whatsApp, Date dataCadastro) {
        this.idPaciente = idPaciente;
        this.whatsApp = whatsApp;
        this.dataCadastro = dataCadastro;
    }

    public Paciente() {
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.idPaciente);
        hash = 43 * hash + Objects.hashCode(this.whatsApp);
        hash = 43 * hash + Objects.hashCode(this.dataCadastro);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paciente other = (Paciente) obj;
        if (!Objects.equals(this.whatsApp, other.whatsApp)) {
            return false;
        }
        if (!Objects.equals(this.idPaciente, other.idPaciente)) {
            return false;
        }
        if (!Objects.equals(this.dataCadastro, other.dataCadastro)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Paciente{idPaciente=").append(idPaciente);
        sb.append(", whatsApp=").append(whatsApp);
        sb.append(", dataCadastro=").append(dataCadastro);
        sb.append('}');
        return sb.toString();
    }

}
