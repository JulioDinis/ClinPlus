package org.openjfx.mapper;

import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.entities.Aporte;

import java.util.List;
import java.util.stream.Collectors;

public class AporteMapper {
    public AporteDTO toDto(Aporte entity){
        AporteDTO dto = new AporteDTO();
        dto.setData(entity.getData());
        dto.setId(entity.getId());
        dto.setValor(entity.getValor());
        dto.setDescricao(entity.getDescricao());
        dto.setColaboradorNome(entity.getColaborador().getNome());
        dto.setColaborador(entity.getColaborador());
        dto.setCaixaMensal(entity.getCaixaMensal());
        return dto;
    }
    public Aporte toEntity(AporteDTO dto){
        Aporte entity = new Aporte();
        entity.setId(dto.getId());
        entity.setData(dto.getData());
        entity.setValor(dto.getValor());
        entity.setDescricao(dto.getDescricao());
        entity.setColaborador(dto.getColaborador());
        entity.setCaixaMensal(dto.getCaixaMensal());
        return  entity;
    }

    public List<AporteDTO> toDto(List<Aporte> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
    public List<Aporte> toEntity(List<AporteDTO> list){
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
