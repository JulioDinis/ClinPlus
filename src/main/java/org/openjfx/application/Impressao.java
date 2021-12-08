package org.openjfx.application;

import javax.print.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

// Essa classe é a responsavel pela impressao. Ela detecta a impressora
// instalada, recebe o texto e o imprime.
public class Impressao {

    public static void main(String[] args) {
        Impressao impressao = new Impressao();
        impressao.imprime("TESTE IMPRESSÂO");
    }

    // variavel estatica porque será utilizada por inumeras threads
    private static PrintService impressora;

    public Impressao() {

        detectaImpressoras();

    }

    // O metodo verifica se existe impressora conectada e a
    // define como padrao.
    public void detectaImpressoras() {

        try {

            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
            for (PrintService p : ps) {

                System.out.println("Impressora encontrada: " + p.getName());

                if (p.getName().contains("Text") || p.getName().contains("Generic") || p.getName().contains("PDF")) {

                    System.out.println("Impressora Selecionada: " + p.getName());
                    impressora = p;
                    break;

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public synchronized boolean imprime(String texto) {

        // se nao existir impressora, entao avisa usuario
        // senao imprime texto
        if (impressora == null) {

            String msg = "Nennhuma impressora foi encontrada. Instale uma impressora padrão \r\n(Generic Text Only) e reinicie o programa.";
            System.out.println(msg);

        } else {

            System.out.println("Imprimindo...");

            try {

                DocPrintJob dpj = impressora.createPrintJob();
                InputStream stream = new ByteArrayInputStream(texto.getBytes());

                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc doc = new SimpleDoc(stream, flavor, null);
                dpj.print(doc, null);

                System.out.println("0la");
                return true;

            } catch (PrintException e) {
                System.out.println("ola");
                e.printStackTrace();

            }

        }

        return false;

    }

}