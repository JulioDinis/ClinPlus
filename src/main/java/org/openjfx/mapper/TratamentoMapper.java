package org.openjfx.mapper;

import org.openjfx.model.dto.TratamentoDTO;
import org.openjfx.model.entities.Tratamento;

import java.util.List;
import java.util.stream.Collectors;

public class TratamentoMapper {

    public Tratamento toEntity(TratamentoDTO dto) {
        Tratamento entity = new Tratamento();
        entity.setIdTratamento(dto.getIdTratamento());
        entity.setDataAprovacao(dto.getDataAprovacao());
        entity.setDesconto(dto.getDesconto());
        entity.setPaciente(dto.getPaciente());
        entity.setIdTratamento(dto.getIdTratamento());
        entity.setDataOrcamento(dto.getDataOrcamento());
        entity.setTotal(dto.getTotal());
        entity.setValidadeOrcamento(dto.getValidadeOrcamento());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public TratamentoDTO toDto(Tratamento entity) {
        TratamentoDTO dto = new TratamentoDTO();
        dto.setIdTratamento(entity.getIdTratamento());
        dto.setDataAprovacao(entity.getDataAprovacao());
        dto.setDesconto(entity.getDesconto());
        dto.setPaciente(entity.getPaciente());
        dto.setIdTratamento(entity.getIdTratamento());
        dto.setDataOrcamento(entity.getDataOrcamento());
        dto.setTotal(entity.getTotal());
        dto.setValidadeOrcamento(entity.getValidadeOrcamento());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<TratamentoDTO> toDto(List<Tratamento> tratamentoList) {
        return tratamentoList.stream().map(this::toDto).collect(Collectors.toList());
    }

}
