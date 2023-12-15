package com.example.matrixcalculator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс для записи пользовательских матриц в файлы CSV.
 */
public class MatrixWriter {

    /**
     * Метод main, в котором создаются две матрицы, сохраняются в файлы CSV, и выводится сообщение об успешном сохранении.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        // Создаем две матрицы для примера
        List<List<Integer>> matrix1 = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(6, 10, 4)
        );

        List<List<Integer>> matrix2 = List.of(
                List.of(9, 8, 7),
                List.of(6, 10, 4),
                List.of(3, 20, 1)
        );

        // Пути к файлам
        String filePathMatrix1 = "matrix1.csv";
        String filePathMatrix2 = "matrix2.csv";

        // Сохраняем матрицы в файлы CSV
        writeMatrixToFile(matrix1, filePathMatrix1);
        writeMatrixToFile(matrix2, filePathMatrix2);

        System.out.println("Матрицы сохранены в файлы: " + filePathMatrix1 + " и " + filePathMatrix2);
    }

    /**
     * Метод для записи матрицы в файл CSV.
     *
     * @param matrix   Матрица для записи.
     * @param filePath Путь к файлу, в который будет записана матрица.
     */
    public static void writeMatrixToFile(List<List<Integer>> matrix, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Integer> row : matrix) {
                StringBuilder line = new StringBuilder();
                for (int value : row) {
                    line.append(value).append(";");
                }
                line.deleteCharAt(line.length() - 1); // Удаляем последний символ (;)
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
