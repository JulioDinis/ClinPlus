package org.openjfx.model.dao;

import java.util.List;

public interface TratamentoDao {
    void insert(TratamentoDao agendaDao);

    void update(TratamentoDao agendaDao);

    void deleteById(Integer idTratamento);

    TratamentoDao findById(Integer idTratamento);

    List<TratamentoDao> findAll();


}
