package org.openjfx.model.dao;

import java.util.List;

public interface CaixaMensalDao {
    void insert(CaixaMensalDao caixaMensal);

    void update(CaixaMensalDao colaborador);

    void deleteById(Integer idFuncionario);
    List<CaixaMensalDao> findAll();

}
