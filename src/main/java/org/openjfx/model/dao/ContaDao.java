package org.openjfx.model.dao;

import java.util.List;

public interface ContaDao {
    void insert(ContaDao agendaDao);

    void update(ContaDao agendaDao);

    void deleteById(Integer idAgenda);

    ContaDao findById(Integer idAgenda);

    List<ContaDao> findAll();


}
