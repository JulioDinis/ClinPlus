package org.openjfx.gui;

import org.openjfx.model.service.ColaboradorService;
import org.openjfx.model.service.ItensTratamentoService;
import org.openjfx.model.service.PacienteService;

public class TelaAtendimentoController {
    private ColaboradorService colaboradorService;
    private PacienteService pacienteService;
    private ItensTratamentoService itensTratamentoService;

    public void setFuncionarioService(ColaboradorService colaboradorService) {

    }

    public void updateTableView() {
    }

    public void setServices(ColaboradorService colaboradorService, PacienteService pacienteService, ItensTratamentoService itensTratamentoService) {
        this.colaboradorService = colaboradorService;
        this.pacienteService = pacienteService;
        this.itensTratamentoService = itensTratamentoService;
    }
}
