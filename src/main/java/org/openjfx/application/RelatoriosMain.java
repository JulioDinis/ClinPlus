package org.openjfx.application;

import net.sf.jasperreports.view.JasperViewer;
import org.openjfx.db.DB;
import org.openjfx.gui.util.Utils;
import org.openjfx.model.service.JasperService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class RelatoriosMain {

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Utils.abrirJrxm("/k");
    }


}
