package com.example.matrixcalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

/**
 * Главный класс приложения, инициализирующий JavaFX и отображающий основное окно калькулятора матриц.
 */
public class HelloApplication extends Application {

    /**
     * Инициализация логгера для записи информации в лог-файл
     */
    static final Logger logger = LogManager.getLogger(HelloController.class.getName());

    /**
     * Метод запуска приложения, инициализирует главное окно и загружает начальное представление.
     *
     * @param stage Главное окно приложения.
     * @throws IOException В случае ошибки при загрузке FXML-ресурса.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Установка кодировки UTF-8
        System.setProperty("file.encoding", "UTF-8");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Matrix Calculator");
        stage.setScene(scene);
        stage.show();

        // Обработчик события закрытия окна
        stage.setOnCloseRequest(event -> logger.info("Пользователь закрыл приложение."));
    }

    /**
     * Точка входа в приложение.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        launch();
    }
}
