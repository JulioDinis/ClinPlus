package org.openjfx.mapper;

import org.openjfx.model.dto.ColaboradorDTO;
import org.openjfx.model.dto.ProcedimentoDTO;
import org.openjfx.model.entities.Atendente;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Procedimento;

import java.util.List;
import java.util.stream.Collectors;

public class ColaboradorMapper {

    public List<ColaboradorDTO> toDto(List<Colaborador> procedimentoList) {

        return procedimentoList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Colaborador toEntityColaborador(ColaboradorDTO dto) {
        Colaborador entity = new Colaborador();
        entity.setIdPessoa(dto.getIdPessoa());
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setRg(dto.getRg());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setSexo(dto.getSexo());
        entity.setEmail(dto.getEmail());
        entity.setLogradouro(dto.getLogradouro());
        entity.setCidade(dto.getCidade());
        entity.setBairro(dto.getBairro());
        entity.setCep(dto.getCep());
        entity.setUf(dto.getUf());
        entity.setTelefone(dto.getTelefone());
        entity.setAtivo(dto.isAtivo());
        //especifico
        entity.setIdColaborador(dto.getIdFuncionario());
        entity.setDataContrato(dto.getDataContrato());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setSenha(dto.getSenha());
        return entity;
    }

    public ColaboradorDTO toDto(Colaborador entity) {
        ColaboradorDTO dto = new ColaboradorDTO();
        dto.setIdFuncionario(entity.getIdColaborador());
        dto.setDataContrato(entity.getDataContrato());
        dto.setEspecialidadeEspecialista(entity.getEspecialidade(), entity.getNome());
        return dto;
    }

    public Atendente toEntityAtendente(ColaboradorDTO dto) {
        Atendente entity = new Atendente();
        entity.setIdPessoa(dto.getIdPessoa());
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setRg(dto.getRg());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setSexo(dto.getSexo());
        entity.setEmail(dto.getEmail());
        entity.setLogradouro(dto.getLogradouro());
        entity.setCidade(dto.getCidade());
        entity.setBairro(dto.getBairro());
        entity.setCep(dto.getCep());
        entity.setUf(dto.getUf());
        entity.setTelefone(dto.getTelefone());
        entity.setAtivo(dto.isAtivo());
        //especifico
        entity.setIdAtendente(dto.getIdFuncionario());
        entity.setDataContrato(dto.getDataContrato());
        entity.setSenha(dto.getSenha());
        return entity;
    }
}
