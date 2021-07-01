    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package org.openjfx.gui.util;

    import javafx.event.ActionEvent;
    import javafx.scene.Node;
    import javafx.scene.control.DatePicker;
    import javafx.scene.control.TableCell;
    import javafx.scene.control.TableColumn;
    import javafx.stage.Stage;
    import javafx.util.StringConverter;

    import java.text.SimpleDateFormat;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.*;

    /**
     * @author julio
     */
    public class Utils {

        public static Stage currentStage(ActionEvent event) {
            return (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        public static Integer tryParseToInt(String str) {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
                return null;
            }
        }

        public static Double tryParseDouble(String str) {
            try {
                return Double.parseDouble(str);
            } catch (Exception e) {
                return null;
            }
        }

        public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
            tableColumn.setCellFactory(column -> {
                TableCell<T, Date> cell = new TableCell<T, Date>() {
                    private SimpleDateFormat sdf = new SimpleDateFormat(format);

                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(sdf.format(item));
                        }
                    }
                };
                return cell;
            });
        }

        public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
            tableColumn.setCellFactory(column -> {
                TableCell<T, Double> cell = new TableCell<T, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            Locale.setDefault(Locale.US);
                            setText(String.format("%." + decimalPlaces + "f", item));
                        }
                    }
                };
                return cell;
            });
        }

        public static void formatDatePicker(DatePicker datePicker, String format) {
            datePicker.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);

                {

                   datePicker.setPromptText(format.toLowerCase());
                }

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
        }

        /**
         * @return HashMap <String, String> contendo os estados brasileiros.
         */
        public static HashMap<String, String> getEstados() {
            HashMap<String, String> estados = new HashMap<>();
            estados.put("AC", "Acre");
            estados.put("AL", "Alagoas");
            estados.put("AP", "Amapá");
            estados.put("AM", "Amazonas");
            estados.put("BA", "Bahia");
            estados.put("CE", "Ceará");
            estados.put("DF", "Distrito Federal");
            estados.put("ES", "Espírito Santo");
            estados.put("GO", "Goiás");
            estados.put("MA", "Maranhão");
            estados.put("MT", "Mato Grosso");
            estados.put("MS", "Mato Grosso do Sul");
            estados.put("MG", "Minas Gerais");
            estados.put("PA", "Pará");
            estados.put("PB", "Paraíba");
            estados.put("PR", "Paraná");
            estados.put("PE", "Pernambuco");
            estados.put("PI", "Piauí");
            estados.put("RJ", "Rio de Janeiro");
            estados.put("RN", "Rio Grande do Norte");
            estados.put("RS", "Rio Grande do Sul");
            estados.put("RO", "Rondônia");
            estados.put("RR", "Roraima");
            estados.put("SC", "Santa Catarina");
            estados.put("SP", "São Paulo");
            estados.put("SE", "Sergipe");
            estados.put("TO", "Tocantins");
            return estados;
        }

        /**
         * @return Retorna uma lista de Strings formatadas em <SIGLA> - <Nome do estado>
         */
        public static List<String> getSiglaEstados() {
            HashMap<String, String> estados = getEstados();

            Set<Map.Entry<String, String>> fields = estados.entrySet();

            Iterator<Map.Entry<String, String>> iterator = fields.iterator();

            List<String> siglasEstados = new ArrayList<>();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                siglasEstados.add(String.format("%s - %s", entry.getKey(), entry.getValue()));
            }

            Collections.sort(siglasEstados);
            return siglasEstados;
        }

        public static List<String> getSexo() {
            List<String> listSexo = new ArrayList<>();
            listSexo.add("Masculino");
            listSexo.add("Feminino");
            return listSexo;
        }

    }















