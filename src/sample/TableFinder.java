package sample;

public class TableFinder {
    private static char[][] table = new char[32][32];  // Таблица Вижинера

    /**
     * Поиск соответствующих символов в таблице Вижинера
     * @param key Ключ
     * @param closedText Закрытый текст
     * @return Открытый текст
     */
    public static String find(String key, String closedText) {
        generateTable();  // Генерация таблицы Вижинера
        String text = findLetters(key, closedText);  // Расшифровка по ключу
        return text;
    }

    /**
     * Генерация таблицы Вижинера
     */
    private static void generateTable() {
        for (int row = 0; row < 32; row++) {
            int off = row;
            for (int col = 0; col < 32; col++) {
                if(off == 32)
                    off = 0;
                table[row][col] = (char)(1040 + off);
                off++;
            }
        }
    }

    /**
     * Нахождение соответствий в таблице Вижинера
     * @param key Ключ шифрования
     * @param closedText Закрытый текст
     * @return Открытый текст
     */
    private static String findLetters(String key, String closedText) {
        String text = "";
        int row = 0;
        int keyLetter = 0;

        for (int textLetter = 0; textLetter < closedText.length(); textLetter++) {
            if (keyLetter >= key.length())
                keyLetter = 0;
                for (int col = 0; col < table.length; col++)
                    if (key.charAt(keyLetter) == table[row][col]) {
                        for (int tableRow = 0; tableRow < table.length; tableRow++) {
                            if (table[tableRow][col] == closedText.charAt(textLetter)) {
                                text += table[tableRow][0];
                                break;
                            }
                        }
                        break;
                    }
            keyLetter++;
        }
        return text;
    }


}
