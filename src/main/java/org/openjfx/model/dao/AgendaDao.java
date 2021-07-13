package org.openjfx.model.dao;

import java.util.List;

public interface AgendaDao {
    void insert(AgendaDao agendaDao);

    void update(AgendaDao agendaDao);

    void deleteById(Integer idAgenda);

    AgendaDao findById(Integer idAgenda);

    List<AgendaDao> findAll();


}
