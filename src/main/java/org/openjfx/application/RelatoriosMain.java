package org.openjfx.application;

import net.sf.jasperreports.view.JasperViewer;
import org.openjfx.db.DB;
import org.openjfx.model.service.JasperService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class RelatoriosMain {

    public static void main(String[] args) throws SQLException, FileNotFoundException {
       abrirJrxm("2");

    }

    private static void abrirJrxm(String name) throws SQLException, FileNotFoundException {
        Connection connection = DB.getConnection("Report");

        JasperService service = new JasperService();

        service.abrirJasperView("../resources/org/openjfx/relatorios/jrxml/colaboradores.jrxml", connection);

        connection.close();

    }
}
