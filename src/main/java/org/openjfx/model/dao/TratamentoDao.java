package org.openjfx.model.dao;

import org.openjfx.model.entities.Tratamento;

import java.util.List;

public interface TratamentoDao {
    int insert(Tratamento tratamento);

    int update(Tratamento tratamento);

    void deleteById(Integer idTratamento);

    Tratamento findById(Integer idTratamento);

    List<Tratamento> findAll();


}
