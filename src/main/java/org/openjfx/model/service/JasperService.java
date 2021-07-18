package org.openjfx.model.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

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

    public void abrirJasperView(String jrxml, Connection connection) throws FileNotFoundException {

        JasperReport report = compilarJrxml(jrxml);
        System.out.println(report);


//        try {
////            JasperPrint print = JasperFillManager.fillReport(report, this.params, connection);
////            JasperViewer viewer = new JasperViewer(print);
////            viewer.setVisible(true);
//        } catch (JRException e) {
//            e.printStackTrace();
//        }

    }

    private JasperReport compilarJrxml(String arquivo) throws FileNotFoundException {

        System.out.println("Abrir -->" + arquivo);
        InputStream report = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        report = getClass().getClassLoader().getResourceAsStream("org/openjfx/application/texto.txt");
        System.out.println(report);

        if (report != null) {
            try {
                return JasperCompileManager.compileReport(report);
            } catch (JRException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Não conseguiu abrir!");
        }

        return null;
    }

}
