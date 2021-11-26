package org.openjfx.mapper;

import jdk.jshell.execution.Util;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.dto.ContaDTO;
import org.openjfx.model.entities.Aporte;
import org.openjfx.model.entities.Conta;

import java.util.List;
import java.util.stream.Collectors;

public class ContaMapper {
    public ContaDTO toDto(Conta entity) {
        ContaDTO dto = new ContaDTO();
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setIdConta(entity.getIdConta());
        dto.setValor(entity.getValor());
        dto.setDescricao(entity.getDescricao());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setObservacao(entity.getObservacao());
        dto.setCaixaMensal(entity.getCaixaMensal());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setValorPagamento(entity.getValorPagamento());
        return dto;
    }

    public Conta toEntity(ContaDTO dto) {
        Conta entity = new Conta();
        entity.setIdConta(dto.getIdConta());
        entity.setDataCadastro(dto.getDataCadastro());
        entity.setValor(dto.getValor());
        entity.setDescricao(dto.getDescricao());
        entity.setDataVencimento(dto.getDataVencimento());
        entity.setValorPagamento(dto.getValorPagamento());
        entity.setDataPagamento(dto.getDataPagamento());
        entity.setObservacao(dto.getObservacao());
        entity.setCaixaMensal(dto.getCaixaMensal());
        return entity;
    }

    public List<ContaDTO> toDto(List<Conta> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Conta> toEntity(List<ContaDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
