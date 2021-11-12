package org.openjfx.model.dao;

import org.openjfx.model.entities.Aporte;

import java.util.List;

public interface AporteDao {
    void insert(Aporte aporte);

    void update(Aporte aporte);

    void deleteById(Integer idAporte);

    Aporte findById(Integer idAporte);

    List<Aporte> findAll();

}
