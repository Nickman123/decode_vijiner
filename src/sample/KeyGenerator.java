package sample;

import java.util.ArrayList;
import java.util.HashMap;


public class KeyGenerator {
    private ArrayList<String> charTable = new ArrayList<>();
    private int period;
    private char alphabet [] = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    public KeyGenerator(ArrayList<String> charTable, int period) {
        this.charTable = charTable;
        this.period = period;
    }

    /**
     * Генерация ключей определенной длины
     * @return Список возможных ключей
     */
    public ArrayList<String> generateKeys() {
        ArrayList<Integer> shifts = findShifts();  // Сдвиги столбцов относительно первого
        ArrayList<String> keys = findKeys(shifts);  // 32 возможных ключа
        return keys;
    }


    /**
     * Поиск сдвигов для всех групп относительно первой
     * @return Список сдвигов
     */
    private ArrayList<Integer> findShifts() {
        ArrayList<Integer> shifts = new ArrayList<>();
        double charsCountFirstGroup = 0;
        HashMap<Character, Integer> mapFirstGroup = new HashMap<>();  // Карта для подсчета частоты букв в первой группе (col = 0)

        // Рассчет характеристик перового столбца
        int col = 0;
        for (int row = 0; row < charTable.size(); row++)
            if((charTable.get(row).length() - 1) - col >= 0) {
                char letter = charTable.get(row).charAt(col);
                charsCountFirstGroup++;

                Integer letterFreq = mapFirstGroup.get(letter);  // Расчет числа вхождений букв
                if (letterFreq != null)
                    mapFirstGroup.put(letter, new Integer(letterFreq + 1));
                else
                    mapFirstGroup.put(letter, 1);
            }

        // Рассчитываем сдвиги относительно первого столбца
        for (int group = 1; group < period; group++)
            shifts.add(findGroupShift(charsCountFirstGroup, mapFirstGroup, group));

        return shifts;
    }

    /**
     * Поиск сдвига для одной группы относительно первой
     * @param charsCountFirstGroup Количество символов в первой группе
     * @param mapFirstGroup Буквы первой группы
     * @param group Номер группы для поиска сдвига
     * @return Сдвиг
     */
    private int findGroupShift(double charsCountFirstGroup, HashMap<Character, Integer> mapFirstGroup, int group) {
        int toShift = 0;  // Необходимое смещение
        double maxIndex = 0;  // Максимальный индекс

        for (int shift = 0; shift < alphabet.length; shift++) {
            double charsCount = 0;
            HashMap<Character, Integer> map = new HashMap<>();

            // Расчет характерстик группы (столбца)
            for (int row = 0; row < charTable.size(); row++)
                if ((charTable.get(row).length() - 1) - group >= 0) {
                    char letter = charTable.get(row).charAt(group);
                    charsCount++;

                    Integer letterFreq = map.get(letter);  // Расчет числа вхождений букв
                    if (letterFreq != null)
                        map.put(letter, letterFreq + 1);
                    else
                        map.put(letter, 1);
                }

            double freqMultipleSum = 0;
            double index = 0;

            for (char letter : mapFirstGroup.keySet()) {
                Integer freqFirstGroup = mapFirstGroup.get(letter);
                Integer freqCurrentGroup = map.get(letter);
                if (freqCurrentGroup == null)
                    freqCurrentGroup = 0;
                freqMultipleSum += freqFirstGroup * freqCurrentGroup;
            }

            index = freqMultipleSum / (charsCountFirstGroup * charsCount);  // Нахождение индекса совпадения;
            // Поиск максимального индекса
            if (index > maxIndex) {
                maxIndex = index;
                toShift = shift;
            }
            moveLetters(1, group);  // Сдвиг столбца
        }
        return toShift;
    }


    /**
     * Осуществляем сдвиг символов столбца
     * @param shift Величина сдвига
     * @param group Номер группы (столбца)
     */
    private void moveLetters(int shift, int group) {

        // Проход по нужному столбцу
        for (int row = 0; row < charTable.size(); row++)
            if((charTable.get(row).length() - 1) - group >= 0) {
                char string[] = charTable.get(row).toCharArray();  // Преобразование строки в символьный массив

                int letterInAlphabetId = 0;
                for (int letter = 0; letter < alphabet.length; letter++)
                    if (string[group] == alphabet[letter])
                        letterInAlphabetId = letter;

                int diff = letterInAlphabetId - shift;  // Сдвиг
                if (diff >= 0)
                    string[group] = alphabet[diff];
                else
                    string[group] = alphabet[alphabet.length + diff];

                charTable.set(row, String.valueOf(string));  // Задаем новое значение столбца
            }
    }

    /**
     * Генерация 32 возможных ключей
     * @param shifts Сдвиги столбцов относительно первого
     * @return Сгенерированные ключи
     */
    private ArrayList<String> findKeys(ArrayList<Integer> shifts) {
        // Подготовка сдвигов
        for (int shift = 0; shift < shifts.size(); shift++)
            shifts.set(shift, alphabet.length - shifts.get(shift));

        ArrayList<String> keys = new ArrayList<>();

        for (int letter = 0; letter < alphabet.length; letter++) {
            String newKey = alphabet[letter] + "";

            for (int group = 0; group < shifts.size(); group++) {
                int difference = letter - shifts.get(group);  // Сдвиг
                if (difference >= 0)
                    newKey += alphabet[difference];
                else
                    newKey += alphabet[alphabet.length + difference];
            }

            keys.add(newKey);  // Добавляем новые ключи
        }
        return keys;
    }
}
