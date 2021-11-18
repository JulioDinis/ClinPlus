package org.openjfx.mapper;

import org.openjfx.model.dto.CaixaMensalDTO;
import org.openjfx.model.entities.CaixaMensal;

import java.util.List;
import java.util.stream.Collectors;

public class CaixaMensalMapper {

    public CaixaMensal toEntity(CaixaMensalDTO dto) {
        CaixaMensal entity = new CaixaMensal();
        entity.setMes(dto.getMes());
        entity.setAno(dto.getAno());
        entity.setStatus(dto.getStatus());
        entity.setSaldoFinal(dto.getSaldoFinal());
        entity.setSaldoInicial(dto.getSaldoInicial());
        return entity;
    }

    public CaixaMensalDTO toDto(CaixaMensal entity) {
        CaixaMensalDTO dto = new CaixaMensalDTO();
        dto.setMes(entity.getMes());
        dto.setAno(entity.getAno());
        dto.setStatus(entity.getStatus());
        dto.setSaldoFinal(entity.getSaldoFinal());
        dto.setSaldoInicial(entity.getSaldoInicial());
        return dto;
    }

    public List<CaixaMensalDTO> toDto(List<CaixaMensal> list){
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
    public List<CaixaMensal> toEntity(List<CaixaMensalDTO> list){
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
}
