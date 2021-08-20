package org.openjfx.mapper;

import org.openjfx.model.dto.ItensTratamentoDTO;
import org.openjfx.model.entities.ItensTratamento;

import java.util.List;
import java.util.stream.Collectors;

public class ItensTratamentoMapper {

    public static ItensTratamento toEntity(ItensTratamentoDTO dto){
        ItensTratamento entity = new ItensTratamento();
        entity.setTratamento(dto.getTratamento());
        entity.setProcedimento(dto.getProcedimento());
        entity.setNrItem(dto.getNrItem());
        entity.setDataExecucao(dto.getDataExecucao());
        entity.setQuantidade(dto.getQuantidade());
        entity.setValor(dto.getValor());
        return  entity;
    }

    public ItensTratamentoDTO toDto(ItensTratamento entity) {
       try{
           ItensTratamentoDTO dto = new ItensTratamentoDTO();
           dto.setProcedimento(entity.getProcedimento());
           dto.setTratamento(entity.getTratamento());
           dto.setNrItem(entity.getNrItem());
           dto.setQuantidade(entity.getQuantidade());
           dto.setValor(entity.getValor());
           dto.setDataExecucao(entity.getDataExecucao());
           dto.setDescricao(entity.getProcedimento().getDescricao());
           return dto;
       }catch (NullPointerException ex){
           return null;
       }

    }

    public List<ItensTratamentoDTO> toDto(List<ItensTratamento> itensTratamentoList) {
        return itensTratamentoList.stream().map(this::toDto).collect(Collectors.toList());
    }

}
