package org.openjfx.application;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EnviarSMS {
    public static void main(String[] args) throws IOException {
        sendSMS("18998234984", "Julio sua Consulta foi agendada para 12/12/21 as 14:00 horas");
    }

    public static void sendSMS(String receiver, String content) throws IOException {
        URL url = new URL("https://sms.comtele.com.br/api/v2/send");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.setRequestProperty("content-type", "application/json");


        String data = "{\"Receivers\":\" " + receiver + "\",\"Content\":\""+content+ "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }

}
