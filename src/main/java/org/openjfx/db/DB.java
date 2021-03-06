package org.openjfx.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection connection = null;

    public static Connection getConnection(String abridor) {
        if (connection == null) {
            try {

                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
                System.out.println("Conexão Aberta por: " + abridor);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection(String fechador) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão Fechada" + fechador);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("src/db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
