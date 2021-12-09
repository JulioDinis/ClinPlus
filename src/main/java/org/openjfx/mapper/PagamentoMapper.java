package org.openjfx.mapper;

import org.openjfx.model.dto.PagamentoDTO;
import org.openjfx.model.entities.Pagamento;

import java.util.List;
import java.util.stream.Collectors;

public class PagamentoMapper {
    public PagamentoDTO toDto(Pagamento entity) {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setTratamento(entity.getTratamento());
        dto.setNumeroParcela(entity.getNumeroParcela());
        dto.setValor(entity.getValor());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setFormaPagamento(entity.getFormaPagamento());
        dto.setPaciente(entity.getTratamento().getPaciente().getNome());
        return dto;
    }

    public Pagamento toEntity(PagamentoDTO dto) {
        Pagamento entity = new Pagamento();
        entity.setTratamento(dto.getTratamento());
        entity.setValor(dto.getValor());
        entity.setDataPagamento(dto.getDataPagamento());
        entity.setFormaPagamento(dto.getFormaPagamento());
        return entity;
    }

    public List<PagamentoDTO> toDto(List<Pagamento> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Pagamento> toEntity(List<PagamentoDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
