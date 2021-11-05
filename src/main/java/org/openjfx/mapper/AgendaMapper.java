package org.openjfx.mapper;

import org.openjfx.model.dto.AgendaDTO;
import org.openjfx.model.entities.Agenda;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.PacienteService;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaMapper {
    private static PacienteService pacienteService = new PacienteService();
    private static ColaboradorService especialistaService = new ColaboradorService();

    public static Agenda toEntity(AgendaDTO dto) {
        Agenda entity = new Agenda();
        entity.setId(dto.getId());
        entity.setData(dto.getData());
        entity.setPaciente(pacienteService.findById(dto.getIdPaciente()));
        entity.setEspecialista(especialistaService.findById(dto.getIdEspecialista()));
        entity.setHorario(dto.getHorario());
        entity.setObservacao(dto.getObservacao());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public AgendaDTO toDto(Agenda entity) {
        try {
            AgendaDTO dto = new AgendaDTO();
            dto.setData(entity.getData());
            dto.setHorario(entity.getHorario());
            dto.setId(entity.getId());
            dto.setEspecialista(entity.getEspecialista().getNome());
            dto.setIdEspecialista(entity.getEspecialista().getIdColaborador());
            dto.setObservacao(entity.getObservacao());
            dto.setStatus(entity.getStatus());
            if (entity.getPaciente() != null) {
                dto.setIdPaciente(entity.getPaciente().getIdPaciente());
                dto.setPaciente(entity.getPaciente().getNome());
            } else {
                dto.setIdPaciente(0);
                dto.setPaciente("");
                dto.setStatus("LIVRE");
            }
            return dto;
        } catch (NullPointerException ex) {
            return null;
        }
    }
    public List<AgendaDTO> toDto(List<Agenda> agendaList, Date data, Colaborador especialista) {
        List<Time> times = new ArrayList<>();
        List<Agenda> list;
        List<Time> aux = new ArrayList<>();
        Agenda a;
        for (int i = 8; i < 19; i++) {
            Time t = Time.valueOf("" + i + ":00:00");
            times.add(t);
        }
        if (agendaList.isEmpty()) {
            list = new ArrayList<>();
            for (Time horario : times) {
                a = new Agenda();
                a.setEspecialista(especialista);
                a.setData(data);
                a.setHorario(horario);
                list.add(a);
            }
            System.out.println("A lista" + list);
        } else {
            list= new ArrayList<>();
            a = agendaList.get(0);
            if (times.size() > agendaList.size()) {
                for (Agenda agenda : agendaList) {
                    aux.add(agenda.getHorario());
                    list.add(agenda);
                }
                for (Time horario : times) {
                    if (!aux.contains(horario)) {
                        Agenda novaEntrada = new Agenda();
                        novaEntrada.setHorario(horario);
                        novaEntrada.setData(a.getData());
                        novaEntrada.setEspecialista(a.getEspecialista());
                        list.add(novaEntrada);
                    }
                }
            }
            Collections.sort(list);
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}
