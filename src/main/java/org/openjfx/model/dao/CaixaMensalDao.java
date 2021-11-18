package org.openjfx.model.dao;

import org.openjfx.model.entities.CaixaMensal;

import java.util.List;

public interface CaixaMensalDao {
    void insert(CaixaMensal caixaMensal);

    void update(CaixaMensal caixaMensal);

    void deleteById(Integer mes, Integer ano);

    CaixaMensal findById(Integer mes, Integer ano);
    List<CaixaMensal> findAllAberto();

    List<CaixaMensal> findAll();

}
