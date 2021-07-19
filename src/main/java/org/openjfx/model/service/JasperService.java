package org.openjfx.model.service;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.openjfx.gui.util.Alerts;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JasperService {

    private Map<String, Object> params = new LinkedHashMap<>();

    public void addParams(String key, Object value) {
        this.params.put(key, value);
    }

    public void abrirJasperView(String jrxml, Connection connection) {

        JasperReport report = compilarJrxml(jrxml);
        System.out.println(report);


        try {
            JasperPrint print = JasperFillManager.fillReport(report, this.params, connection);
            JasperViewer viewer = new JasperViewer(print,  false);
            viewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    private JasperReport compilarJrxml(String arquivo) {
        System.out.println(getClass().getClassLoader());
        InputStream report = getClass().getResourceAsStream(arquivo);
        if (report != null) {
            try {
                return JasperCompileManager.compileReport(report);
            } catch (JRException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Falha ao Carregar Imagem");
        }

        return null;
    }

}
