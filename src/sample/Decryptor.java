package sample;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Decryptor {

    private String textToDecrypt;

    public Decryptor(String textToDecrypt) {
        this.textToDecrypt = textToDecrypt;
    }

    /**
     * Поиск ключей шифрования
     * @return Возможные ключи шифрования
     */
    public ArrayList<String> findKeys() {
        HashMap<Integer, Integer> periodsCounters = new HashMap<>();  // Счетчики периодов для выбора наилучшего
        ArrayList<String> chars;
        ArrayList<String> keys = new ArrayList<>();  // Возможные ключи шифрования
        for (int index = 0; index < textToDecrypt.length()-3; index++) {
            ArrayList<Integer> distances;  // Расстояния между триграммами
            distances = testKasiska(index);

            if (distances != null) {
                int nod;
                if (distances.size() == 1)
                    nod = distances.get(0);
                else nod = checkNod(distances);
                if (nod > 1 && nod < 10) {  // Нахождение НОД
                    ArrayList<String> charTable = checkIndexes(nod);
                    if (charTable != null) { // Нахождение индексов соответсвия
                        Integer nodCount = periodsCounters.get(nod);
                        if (nodCount == null)
                            periodsCounters.put(nod, 1);
                        else
                            periodsCounters.put(nod, nodCount+1);
                    }
                }
            }

        }
            int maxCounter = 0;
            int maxPeriod = 0;
            // Нахождение подоходящего периода
            for (int periodCounter : periodsCounters.keySet()) {
                if (periodsCounters.get(periodCounter) > maxCounter) {
                    maxCounter = periodsCounters.get(periodCounter);
                    maxPeriod = periodCounter;
                }
            }
            if (maxPeriod == 0)
                keys.add("Не удалось найти ключи шифрования!");
            else {
                System.out.println("Period: " + maxPeriod);
                chars = checkIndexes(maxPeriod);
                // Генерация ключей
                KeyGenerator generator = new KeyGenerator(chars, maxPeriod);
                keys = generator.generateKeys();
            }
        return keys;
    }

    /**
     * Нахождение индексов соответствия
     * @param period Период шифрования
     * @return Подходит ли период
     */
    private ArrayList<String> checkIndexes(int period) {
        ArrayList <String> charTable = new ArrayList<>();
        String newRow;

        // Занесение данных в таблицу
        for (int group = 0; group < textToDecrypt.length(); group+= period) {
            newRow = "";  // Новая строка

            if (group + period <= textToDecrypt.length() - 1) {
                for (int groupSym = 0; groupSym < period; groupSym++)
                    newRow += textToDecrypt.charAt(group + groupSym);
            }
            else
                for (int groupSym = 0; groupSym < (textToDecrypt.length() - group); groupSym++)
                    newRow += textToDecrypt.charAt(group + groupSym);

            charTable.add(newRow); // Добавление новой строки
        }
        if (calcIndexes(period, charTable))  // Рассчитываем индексы
            return charTable;
        else return null;

    }

    /**
     * Расчет индексов для каждой из групп
     * @param period Период шифрования
     * @param charTable Таблица символов
     * @return Являются ли индексы верными
     */
    private boolean calcIndexes(int period, ArrayList<String> charTable) {
        double index = 0, charsCount, maxIndex = 0.0529, minIndex = 0.0312;
        HashMap <Character, Integer> map = new HashMap<>();

        // Рассчитываем индексы по столбцам
        for (int col = 0; col < period; col++) {
            charsCount = 0; map.clear();
            for (int row = 0; row < charTable.size(); row++)
                if((charTable.get(row).length() - 1) - col >= 0) {
                    char letter = charTable.get(row).charAt(col);
                    charsCount++;

                    Integer letterFreq = map.get(letter);  // Расчет числа вхождений букв
                    if (letterFreq != null)
                        map.put(letter, new Integer(letterFreq + 1));
                    else
                        map.put(letter, 1);
                }

            double freqSum = 0;
            for (int letterFreq : map.values())
                freqSum += letterFreq * (letterFreq - 1);

            index = freqSum/(charsCount * (charsCount - 1));
            // Проверка индекса
            if (abs(index - maxIndex) > abs(index - minIndex))
                return false;

        }
        return true;
    }

    /**
     * Тест Касиски
     * @param trigramIndex Начальная позиция первой триграммы
     * @return Период шифрования
     */
    private ArrayList<Integer> testKasiska(int trigramIndex) {
        ArrayList<Integer> distances = new ArrayList<>();

        int lastPosition = trigramIndex; // Последняя позиция триграммы
        int trigramsAmount = 1; // Количество триграмм

        String trigram = textToDecrypt.substring(trigramIndex, trigramIndex + 3);  // Берём триграмму

        // Поиск расстояний
        while(true) {
            int index = textToDecrypt.indexOf(trigram, lastPosition + 3); // Поиск триграмм в строке

            if (index != -1) {
                trigramsAmount++;
                distances.add(index - lastPosition);  // Расстояние от предыдущей триграммы
                lastPosition = index;  // Указываем на новую найденную триграмму
            } else
                if (trigramsAmount < 2) // Переходим к другой триграмме
                    return null;
                else
                    break;
        }
        return distances;
    }

    /**
     * Нахождение НОД
     * @param distances Расстояния между триграммами
     * @return НОД
     */
    private int checkNod(ArrayList<Integer> distances) {
        int nod = 1;
        int multiplication;
        for (int i = 0; i < distances.size(); i++) { //Нахождение НОД всех чисел массива
            if (i + 1< distances.size()) {
                for (int j = i + 1; j <= i + 1;j++) {
                    nod = gcd(distances.get(i), distances.get(j));
                }
                distances.set(i+1, nod);
            }
        }

        return nod;
    }

    /**
     * Нахождение НОД двух чисел
     * @param a Первое число
     * @param b Второе число
     * @return
     */
    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }
}
