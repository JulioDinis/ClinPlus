package org.openjfx.mapper;

import org.openjfx.model.dto.ProcedimentoDTO;
import org.openjfx.model.entities.Procedimento;

import java.util.List;
import java.util.stream.Collectors;

public class ProcedimentoMapper {

    public List<ProcedimentoDTO> toDto(List<Procedimento> procedimentoList) {

        return procedimentoList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Procedimento toEntity(ProcedimentoDTO dto) {
        Procedimento entity = new Procedimento();
        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setIdProcedimento(dto.getIdProcedimento());
        entity.setColaborador(dto.getColaborador());
        return entity;
    }

    public ProcedimentoDTO toDto(Procedimento entity) {
        ProcedimentoDTO dto = new ProcedimentoDTO();
        dto.setDescricao(entity.getDescricao());
        dto.setValor(entity.getValor());
        dto.setIdProcedimento(entity.getIdProcedimento());
        dto.setColaborador(entity.getColaborador());
        dto.setNomeEspecialista(entity.getColaborador().getNome() + " | " + entity.getColaborador().getEspecialidade());
        return dto;
    }
}
