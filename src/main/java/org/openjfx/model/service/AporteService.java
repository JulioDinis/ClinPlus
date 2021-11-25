package org.openjfx.model.service;

import org.openjfx.mapper.AporteMapper;
import org.openjfx.model.dao.AporteDao;
import org.openjfx.model.dao.DaoFactory;
import org.openjfx.model.dao.impl.AporteDaoJDBC;
import org.openjfx.model.dto.AporteDTO;
import org.openjfx.model.entities.Atendente;

import java.util.List;

public class AporteService {
    private AporteDao dao = DaoFactory.createAporteDao("Aporte Service");
    private AporteMapper mapper = new AporteMapper();

    public void saveOrUpdate(AporteDTO aporteDTO) {
        if (aporteDTO.getId() == null) {
            dao.insert(mapper.toEntity(aporteDTO));
        } else {
            dao.update(mapper.toEntity(aporteDTO));
        }
    }

    public List<AporteDTO> findAll() {

        return mapper.toDto(dao.findAll());
    }

    public void remove(AporteDTO aporteDTO){

    }

}
