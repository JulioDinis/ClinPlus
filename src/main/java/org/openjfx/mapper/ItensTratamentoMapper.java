package org.openjfx.mapper;

import org.openjfx.model.dto.ItensTratamentoDto;
import org.openjfx.model.entities.ItensTratamento;

import java.util.List;
import java.util.stream.Collectors;

public class ItensTratamentoMapper {

    public static ItensTratamento toEntity(ItensTratamentoDto dto){

        ItensTratamento entity = new ItensTratamento();

        entity.setTratamento(dto.getTratamento());
        entity.setProcedimento(dto.getProcedimento());
        entity.setNrItem(dto.getNrItem());
        entity.setDataExecucao(dto.getDataExecucao());
        entity.setQuantidade(dto.getQuantidade());
        entity.setValor(dto.getValor());
        return  null;
    }

//    public Stock toEntity(StockDTO dto) {
//        // transforma o dto em entity forma manual (existem formas automatizadas)
//        Stock stock = new Stock();
//        stock.setId(dto.getId());
//        stock.setName(dto.getName());
//        stock.setPrice(dto.getPrice());
//        stock.setVariation(dto.getVariation());
//        stock.setDate(dto.getDate());
//        return stock;
//    }

    public ItensTratamentoDto toDto(ItensTratamento entity) {
        ItensTratamentoDto dto = new ItensTratamentoDto();
        dto.setProcedimento(entity.getProcedimento());
        dto.setTratamento(entity.getTratamento());
        dto.setNrItem(entity.getNrItem());
        dto.setQuantidade(entity.getQuantidade());
        dto.setValor(entity.getValor());
        dto.setDataExecucao(entity.getDataExecucao());
        dto.setDescricao(entity.getProcedimento().getDescricao());
        return dto;
    }

    public List<ItensTratamentoDto> toDto(List<ItensTratamento> itensTratamentoList) {
        return itensTratamentoList.stream().map(this::toDto).collect(Collectors.toList());
    }

}
