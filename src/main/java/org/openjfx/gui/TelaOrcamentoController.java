package org.openjfx.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.openjfx.model.entities.Colaborador;
import org.openjfx.model.entities.Procedimento;
import org.openjfx.model.service.ProcedimentoService;

import java.util.List;

public class TelaOrcamentoController {
    private ProcedimentoService service;
    private Colaborador colaboradorLogado;
    private ObservableList<Procedimento> obsList;

    @FXML
    private ListView<Procedimento> listViewProcedimento;

    @FXML
    private TableView<Procedimento> tableViewProcedimento;

    @FXML
    private TableColumn<Procedimento, String> tableColumnDescricao;

    @FXML
    private TableColumn<Procedimento, Double> tableColumnValor;

    public TelaOrcamentoController() {
    }

    public TelaOrcamentoController(ProcedimentoService service, Colaborador colaboradorLogado, ListView<Procedimento> listViewProcedimento, TableView<Procedimento> tableViewProcedimento, TableColumn<Procedimento, String> tableColumnDescricao, TableColumn<Procedimento, Double> tableColumnValor) {
        this.service = service;
        this.colaboradorLogado = colaboradorLogado;
        this.listViewProcedimento = listViewProcedimento;
        this.tableViewProcedimento = tableViewProcedimento;
        this.tableColumnDescricao = tableColumnDescricao;
        this.tableColumnValor = tableColumnValor;
    }

    public ProcedimentoService getService() {
        return service;
    }

    public void setService(ProcedimentoService service) {
        this.service = service;
    }

    public Colaborador getFuncionarioLogado() {
        return colaboradorLogado;
    }

    public void setFuncionarioLogado(Colaborador colaboradorLogado) {
        this.colaboradorLogado = colaboradorLogado;
    }

    public ListView<Procedimento> getListViewProcedimento() {
        return listViewProcedimento;
    }

    public void setListViewProcedimento(ListView<Procedimento> listViewProcedimento) {
        this.listViewProcedimento = listViewProcedimento;
    }

    public TableView<Procedimento> getTableViewProcedimento() {
        return tableViewProcedimento;
    }

    public void setTableViewProcedimento(TableView<Procedimento> tableViewProcedimento) {
        this.tableViewProcedimento = tableViewProcedimento;
    }

    public TableColumn<Procedimento, String> getTableColumnDescricao() {
        return tableColumnDescricao;
    }

    public void setTableColumnDescricao(TableColumn<Procedimento, String> tableColumnDescricao) {
        this.tableColumnDescricao = tableColumnDescricao;
    }

    public TableColumn<Procedimento, Double> getTableColumnValor() {
        return tableColumnValor;
    }

    public void setTableColumnValor(TableColumn<Procedimento, Double> tableColumnValor) {
        this.tableColumnValor = tableColumnValor;
    }

    public void updateList(){

        if (service == null) {
            throw new IllegalStateException("Service was Null");
        }

        List<Procedimento> list = service.findAllAtivos();
        System.out.println(list);
        obsList = FXCollections.observableArrayList(list);
        listViewProcedimento.setItems(obsList);
//        initEditButtons();
//        initRemoveButtons();
    }
}
