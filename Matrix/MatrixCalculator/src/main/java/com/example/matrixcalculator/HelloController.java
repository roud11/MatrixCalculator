package com.example.matrixcalculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Класс контроллера для приложения Matrix Calculator.
 */
public class HelloController {

    /**
     * Инициализация логгера для записи информации в лог-файл
     */
    static final Logger logger = LogManager.getLogger(HelloController.class.getName());

    /**
     * Данные для матриц
     */
    private List<List<Integer>> matrix1Data = null;
    private List<List<Integer>> matrix2Data = null;

    /**
     * Элементы управления из FXML, связанные с этим контроллером
     */
    @FXML
    private Button loadMatrix1Button;

    @FXML
    private Button loadMatrix2Button;

    @FXML
    private Button subtractButton;

    @FXML
    private Button multiplyButton;

    @FXML
    private Label matrix1Label;

    @FXML
    private Label resultLabel;

    @FXML
    private Label matrix2Label;

    @FXML
    private Label determinant1Text;

    @FXML
    private Label determinant2Text;

    @FXML
    private Button addButton;

    /**
     * Инициализирует контроллер, устанавливает действия для кнопок.
     */
    @FXML
    void initialize() {
        logger.info("Пользователь запустил приложение.");
        addButton.setOnAction(event -> add());
        subtractButton.setOnAction(event -> subtract());
        multiplyButton.setOnAction(event -> multiply());

        loadMatrix1Button.setOnAction(event -> loadMatrix(1, matrix1Label));
        loadMatrix2Button.setOnAction(event -> loadMatrix(2, matrix2Label));
    }

    /**
     * Выполняет операцию сложения матриц при нажатии кнопки "Сложение".
     */
    @FXML
    public void add() {
        logger.info("Пользователь нажал на кнопку 'Сложение'.");
        performOperation("Сложение", MatrixOperations::addMatrices);
    }

    /**
     * Выполняет операцию вычитания матриц при нажатии кнопки "Вычитание".
     */
    @FXML
    public void subtract() {
        logger.info("Пользователь нажал на кнопку 'Вычитание'.");
        performOperation("Вычитание", MatrixOperations::subtractMatrices);
    }

    /**
     * Выполняет операцию умножения матриц при нажатии кнопки "Умножение".
     */
    @FXML
    public void multiply() {
        logger.info("Пользователь нажал на кнопку 'Умножение'.");
        performOperation("Умножение", MatrixOperations::multiplyMatrices);
    }

    /**
     * Выполняет указанную операцию над матрицами.
     *
     * @param operation        Название операции (Сложение, Вычитание, Умножение).
     * @param operationFunction Функция, представляющая операцию над матрицами.
     */
    private void performOperation(String operation, MatrixOperation operationFunction) {
        // Проверяем, загружены ли матрицы
        if (matrix1Data == null || matrix2Data == null) {
            showError("Необходимо загрузить матрицы.");
            return;
        }

        // Получаем размеры матриц
        int rows1 = matrix1Data.size();
        int columns1 = matrix1Data.get(0).size();
        int rows2 = matrix2Data.size();
        int columns2 = matrix2Data.get(0).size();

        // Проверяем совместимость размеров матриц для операции сложения или вычитания
        if ((operation.equals("Сложение") || operation.equals("Вычитание")) &&
                (rows1 != rows2 || columns1 != columns2)) {
            showError("Матрицы должны быть одного размера для операции " + operation);
            return;
        }

        try {
            // Выполняем выбранную операцию
            List<List<Integer>> resultMatrix = operationFunction.apply(matrix1Data, matrix2Data);
            // Отображаем результат
            showMatrix(resultMatrix, resultLabel);
            // В случае ошибки выводим сообщение об ошибке
        } catch (IllegalArgumentException e) {
            showError("Невозможно выполнить операцию " + operation + ": " + e.getMessage());
        } catch (NullPointerException e) {
            showError("Одна или обе матрицы равны null");
        }
    }

    /**
     * Проверяет, является ли выбранный файл файлом с матрицей в формате CSV.
     *
     * @param file Выбранный файл.
     * @return True, если файл представляет собой матрицу, в противном случае - false.
     */
    private boolean isMatrixFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int columnCount = -1;

            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                // Разбиваем строку на значения, разделенные ";"
                String[] values = line.split(";");
                if (columnCount == -1) {
                    // Если это первая строка, сохраняем количество столбцов
                    columnCount = values.length;
                } else {
                    if (values.length != columnCount) {
                        // Проверяем, что количество столбцов одинаково в каждой строке
                        return false;
                    }
                }
                // Увеличиваем счетчик строк
                rowCount++;
            }
            // Проверяем, что в файле есть хотя бы две строки (две строки для формирования матрицы)
            return rowCount >= 2;
        } catch (IOException e) {
            // В случае ошибки ввода/вывода возвращаем false и выводим сообщение в лог
            showError("Ошибка при чтении файла: " + e.getMessage());
            return false;
        }
    }

    /**
     * Загружает данные матрицы из файла.
     *
     * @param file Выбранный файл с матрицей.
     * @return Список списков целых чисел, представляющих матрицу.
     * @throws IOException                Возникает при ошибке ввода/вывода при чтении файла.
     * @throws IllegalArgumentException   Возникает при обнаружении некорректного значения в файле.
     */
    private List<List<Integer>> loadMatrixFromFile(File file) throws IOException {
        List<List<Integer>> matrixData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Разбиваем строку на значения, разделенные ";"
                String[] values = line.split(";");
                List<Integer> row = new ArrayList<>();
                for (String value : values) {
                    try {
                        // Парсим значение в целое число и добавляем в строку матрицы
                        row.add(Integer.parseInt(value));
                    } catch (NumberFormatException ex) {
                        // В случае ошибки формата выводим сообщение и бросаем исключение
                        throw new IllegalArgumentException("Некорректное значение в строке: " + line);
                    }
                }
                matrixData.add(row);
            }
        }

        return matrixData;
    }

    /**
     * Загружает матрицу из файла.
     *
     * @param matrixNumber Номер матрицы (1 или 2).
     * @param label        Метка для отображения матрицы.
     */
    private void loadMatrix(int matrixNumber, Label label) {
        logger.info("Пользователь нажал на кнопку 'Загрузить матрицу " + matrixNumber + "'.");
        try {
            // Создаем окно для выбора файла
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
            Stage stage = new Stage();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                // Проверяем, является ли файл матрицей
                if (isMatrixFile(selectedFile)) {
                    List<List<Integer>> matrixData = loadMatrixFromFile(selectedFile);
                    // Сохраняем данные матрицы в соответствующей переменной
                    if (matrixNumber == 1) {
                        matrix1Data = matrixData;
                    } else if (matrixNumber == 2) {
                        matrix2Data = matrixData;
                    }
                    // Отображаем матрицу в интерфейсе
                    showMatrix(matrixData, label);
                } else {
                    showError("Выбранный файл не является матрицей.");
                }
            }
        } catch (Exception e) {
            showError("Произошла ошибка при загрузке матрицы: " + e.getMessage());
        }
    }

    /**
     * Вычисляет и отображает определитель матрицы.
     *
     * @param matrixNumber Номер матрицы (1 или 2).
     */
    private void calculateDeterminant(int matrixNumber) {
        List<List<Integer>> matrixData;
        Label determinantLabel;

        if (matrixNumber == 1) {
            matrixData = matrix1Data;
            determinantLabel = determinant1Text;
            logger.info("Пользователь нажал кнопку 'Определитель 1'.");
        } else {
            matrixData = matrix2Data;
            determinantLabel = determinant2Text;
            logger.info("Пользователь нажал кнопку 'Определитель 2'.");
        }

        if (matrixData == null) {
            showError("Загрузите матрицу " + matrixNumber + " файлом типа '.csv'");
            return;
        }

        int determinant;
        try {
            determinant = MatrixOperations.calculateDeterminant(matrixData);
            determinantLabel.setText("Определитель: " + determinant);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Загружает матрицу 1 из файла.
     *
     * @param ignoredEvent Событие кнопки загрузки матрицы 1.
     */
    @FXML
    public void loadMatrix1(ActionEvent ignoredEvent) {
        loadMatrix(1, matrix1Label);
    }

    /**
     * Загружает матрицу 2 из файла.
     *
     * @param ignoredEvent Событие кнопки загрузки матрицы 2.
     */
    @FXML
    public void loadMatrix2(ActionEvent ignoredEvent) {
        loadMatrix(2, matrix2Label);
    }

    /**
     * Вычисляет и отображает определитель матрицы 1.
     *
     * @param ignoredEvent Событие кнопки вычисления определителя для матрицы 1.
     */
    @FXML
    public void calculateDeterminant1(ActionEvent ignoredEvent) {
        calculateDeterminant(1);
    }

    /**
     * Вычисляет и отображает определитель матрицы 2.
     *
     * @param ignoredEvent Событие кнопки вычисления определителя для матрицы 2.
     */
    @FXML
    public void calculateDeterminant2(ActionEvent ignoredEvent) {
        calculateDeterminant(2);
    }

    /**
     * Отображает матрицу в пользовательском интерфейсе.
     *
     * @param matrixData Матрица для отображения.
     * @param label      Метка для отображения матрицы.
     */
    private void showMatrix(List<List<Integer>> matrixData, Label label) {
        // Проверяем, что матрица не пуста
        if (matrixData == null) {
            return;
        }

        // Формируем строку для отображения матрицы
        StringBuilder matrixString = new StringBuilder();
        for (List<Integer> row : matrixData) {
            for (Integer value : row) {
                // Добавляем значение в строку с табуляцией после каждого элемента
                matrixString.append(value).append("\t");
            }
            // Переходим на новую строку после каждой строки матрицы
            matrixString.append("\n");
        }
        // Устанавливаем сформированную строку в метку интерфейса
        label.setText(matrixString.toString());
    }

    /**
     * Отображает диалоговое окно с сообщением об ошибке и записываем его в файл log4j2.xml.
     *
     * @param message Сообщение об ошибке.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        logger.info(message);
    }

    /**
     * Функциональный интерфейс для представления операции над матрицами.
     */
    @FunctionalInterface
    interface MatrixOperation {
        List<List<Integer>> apply(List<List<Integer>> matrix1, List<List<Integer>> matrix2);
    }
}
