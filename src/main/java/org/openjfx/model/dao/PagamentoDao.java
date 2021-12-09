package org.openjfx.model.dao;

import org.openjfx.model.entities.Pagamento;

import java.util.List;

public interface PagamentoDao {

    void insert(Pagamento agendaDao);

    void update( Pagamento agendaDao);

    void deleteById(Pagamento pagamento);

    List<Pagamento> findAll();


    Pagamento findById(int pagamentoId);
}
