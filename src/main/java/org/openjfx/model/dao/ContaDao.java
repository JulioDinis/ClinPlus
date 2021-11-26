package org.openjfx.model.dao;

import org.openjfx.model.entities.CaixaMensal;
import org.openjfx.model.entities.Conta;

import java.util.List;

public interface ContaDao {
    void insert(Conta conta);

    void update(Conta conta);

    void deleteById(Integer idConta);

    Conta findById(Integer idConta);

    List<Conta> findAll();


    List<Conta> findByCaixaMensal(CaixaMensal caixaMensal);
}
