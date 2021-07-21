package org.openjfx.gui.util;

import java.util.HashMap;
import java.util.Map;

public class ExemploHasMap {
    public static void main(String[] args) {

        Map<String, Integer> campeosMundialFifa = new HashMap<>();

        campeosMundialFifa.put("Brasil", 5);
        campeosMundialFifa.put("Alemanha", 4);
        campeosMundialFifa.put("Italia", 4);
        campeosMundialFifa.put("Uruguai", 3);
        campeosMundialFifa.put("Argentina", 2);
        campeosMundialFifa.put("Espanha", 1);

        System.out.println(campeosMundialFifa);

        campeosMundialFifa.put("Brasil", 6);

        System.out.println(campeosMundialFifa);
        System.out.println(campeosMundialFifa.containsKey("Noroega"));

        System.out.println(campeosMundialFifa.containsValue(6));

        System.out.println(campeosMundialFifa.size());
        for (Map.Entry<String, Integer> entry : campeosMundialFifa.entrySet()) {
            System.out.println(entry.getKey() + "---" + entry.getValue());
        }

        for (String key : campeosMundialFifa.keySet()) {
            System.out.println(key + "***" + campeosMundialFifa.get(key));
        }

        System.out.println(campeosMundialFifa.size());
        campeosMundialFifa.clear();
        System.out.println(campeosMundialFifa.size());
    }
}
