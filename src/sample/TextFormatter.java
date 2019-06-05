package sample;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TextFormatter {

    /**
     * Форматирование открытого текста
     * @param closedText Закрытый текст
     * @param openedText Открытый текст
     * @return Отформатированный открытый текст
     */
    public static String formatText(String closedText, String openedText) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        ArrayList<String> extraChars = new ArrayList<>();
        extraChars.add("-");
        extraChars.add(",");
        extraChars.add(".");
        extraChars.add("!");
        extraChars.add("?");
        extraChars.add(";");
        extraChars.add(":");
        extraChars.add("\"");
        extraChars.add(" ");
        extraChars.add("\n");

        for (int i = 0; i < closedText.length(); i++) {
            String sym = String.valueOf(closedText.charAt(i));
            if (extraChars.contains(sym)) {
                map.put(i, sym);
            }
        }
        StringBuffer buffer = new StringBuffer(openedText);
        for (Integer key : map.keySet()) {
            buffer.insert(key, map.get(key));
        }
        return String.valueOf(buffer);
    }
}
