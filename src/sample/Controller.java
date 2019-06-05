package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextArea consoleField, decryptedText, textToDecrypt;
    @FXML
    private TextField keyField;
    @FXML
    private Button cancelButton, okButton, decryptButton;
    @FXML
    private Label keyFieldLabel, decryptedTextLabel, timerLabel, timerTime;
    @FXML
    private ProgressIndicator progressIndicator;

    private ArrayList<String> keys;  // Ключи шифрования
    private int currentKeyIndex = 0;  // Индекс выбранного ключа
    private String text = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        printInConsole(true, "Добро пожаловать в программу! Начните расшифровывать закрытый текст.");
    }

    /**
     * Вывод информации в консоль
     * @param status Статус информации
     * @param text Информация
     */
    private void printInConsole(boolean status, String text) {
        if (status)
            consoleField.appendText(">> " + text + "\n");
        else
            consoleField.appendText("(Ошибка) >> " + text + "\n");
    }

    /**
     * Расшифровать текст
     * @param event Событие
     */
    @FXML
    public void decryptText(ActionEvent event) {
        currentKeyIndex = 0;
        text = textToDecrypt.getText().replaceAll("[-,.!?;:\" \n]", "");  // Закрытый текст
        if (!text.equals("")) {
            initializeElements(); // Разблокировать элементы интерфейса

            // Начать процесс расшифровки
            startTimer();
            printInConsole(true, "Поиск ключей...");
            Decryptor decryptor = new Decryptor(text);
            keys = decryptor.findKeys();
            System.out.println(keys);

            printInConsole(true, "Возможные ключи шифрования найдены! Проверьте правильность дешифрования!");
            progressIndicator.setProgress(0.5);

            String key = keys.get(currentKeyIndex) + "";
            keyField.setText(key);
            String openedText = TableFinder.find(key, text);  // Нахождение начальной последовательности
            System.out.println(openedText);
            printInConsole(true, "Нахождение соответсвий в таблице Вижинера...");
            decryptedText.setText(TextFormatter.formatText(textToDecrypt.getText(), openedText));
            printInConsole(true, "Проверьте правильность дешифрования!");
        }
        else
            printInConsole(false, "Сначала введите текст для расшифровки!");
    }

    /**
     * Разблокировать элементы интерфейса
     */
    private void initializeElements() {
        keyFieldLabel.setDisable(false);
        keyField.setDisable(false);
        decryptedTextLabel.setDisable(false);
        decryptedText.setDisable(false);
        cancelButton.setDisable(false);
        okButton.setDisable(false);
        progressIndicator.setDisable(false);
        progressIndicator.setProgress(-1);
    }

    /**
     * Включить таймер
     */
    private void startTimer() {
        printInConsole(true, "Запуск таймера...");
        timerLabel.setVisible(false);
        timerTime.setVisible(false);
        Timer.startTimer();
        printInConsole(true, "Таймер успешно запущен!");
    }

    /**
     * Выключить таймер
     */
    private void stopTimer() {
        timerLabel.setVisible(true);
        timerTime.setVisible(true);

        long time = Timer.finishTimer();
        timerTime.setText(time + " сек.");
        printInConsole(true, "Таймер отключен.");

    }

    /**
     * Принять ключ
     * @param event Событие
     */
    public void acceptKey(ActionEvent event) {
        progressIndicator.setProgress(1);  // Дешифрование завершено
        printInConsole(true, "Дешиврование завершено!");
        stopTimer();
    }

    /**
     * Отказаться от ключа
     * @param event Событие
     */
    public void cancelKey(ActionEvent event) {
        printInConsole(true, "Переключение на следующий ключ...");
        currentKeyIndex ++;
        if (currentKeyIndex > 31)
            currentKeyIndex = 0;
        String key = keys.get(currentKeyIndex) + "";
        keyField.setText(key);
        String openedText = TableFinder.find(key, text);  // Нахождение начальной последовательности
        System.out.println(openedText);
        printInConsole(true, "Нахождение соответсвий в таблице Вижинера...");

        decryptedText.setText(TextFormatter.formatText(textToDecrypt.getText(), openedText));
        printInConsole(true, "Проверьте правильность дешифрования!");
    }

}
