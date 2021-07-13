package org.openjfx.model.dao;

import java.util.List;

public interface AporteDao {
    void insert(AporteDao colaborador);

    void update(AporteDao colaborador);

    void deleteById(Integer idAporte);

    AporteDao findById(Integer idAporte);

    List<AporteDao> findAll();

}
