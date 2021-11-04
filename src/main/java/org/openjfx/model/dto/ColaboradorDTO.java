package org.openjfx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorDTO {
    private static final long serialVersionUID = 1L;
    private Integer idPessoa;
    private String nome;
    private String cpf;
    private String rg;
    private Date dataNascimento;
    private String sexo;
    private String email;
    private String logradouro;
    private String cidade;
    private String bairro;
    private String cep;
    private String uf;
    private String telefone;
    private boolean ativo;
    private String especialidade;
    private String senha;
    private Date dataContrato;
    private Date dataDesligamento;
    private Integer idFuncionario;
    private String especialidadeEspecialista;

    public void setEspecialidadeEspecialista(String epecialidade, String especialista){
        this.especialidadeEspecialista = especialidade + " - " + especialista;
    }
}
