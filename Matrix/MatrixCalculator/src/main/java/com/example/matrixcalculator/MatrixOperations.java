package com.example.matrixcalculator;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс, предоставляющий статические методы для выполнения операций с матрицами.
 */
public class MatrixOperations {

    /**
     * Сложение двух матриц.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @return Результирующая матрица, являющаяся суммой matrix1 и matrix2.
     * @throws IllegalArgumentException Если размеры матриц не совпадают.
     */
    public static List<List<Integer>> addMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        // Проверяем, что размеры матриц соответствуют для выполнения операции
        checkMatrixDimensions(matrix1, matrix2);
        // Создаем пустую матрицу для результата
        List<List<Integer>> resultMatrix = new ArrayList<>();
        // Проходим по каждой строке и столбцу и складываем соответствующие элементы
        for (int i = 0; i < matrix1.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < matrix1.get(0).size(); j++) {
                // Складываем элементы из первой и второй матрицы и добавляем в результат
                row.add(matrix1.get(i).get(j) + matrix2.get(i).get(j));
            }
            // Добавляем строку с результатами в итоговую матрицу
            resultMatrix.add(row);
        }

        return resultMatrix;
    }

    /**
     * Вычитание одной матрицы из другой.
     *
     * @param matrix1 Исходная матрица.
     * @param matrix2 Вычитаемая матрица.
     * @return Результирующая матрица, являющаяся разностью matrix1 и matrix2.
     * @throws IllegalArgumentException Если размеры матриц не совпадают.
     */
    public static List<List<Integer>> subtractMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        // Проверяем, что размеры матриц соответствуют для выполнения операции
        checkMatrixDimensions(matrix1, matrix2);
        // Создаем пустую матрицу для результата
        List<List<Integer>> resultMatrix = new ArrayList<>();
        // Проходим по каждой строке и столбцу и вычитаем соответствующие элементы
        for (int i = 0; i < matrix1.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < matrix1.get(0).size(); j++) {
                // Вычитаем элементы из первой матрицы второй и добавляем в результат
                row.add(matrix1.get(i).get(j) - matrix2.get(i).get(j));
            }
            // Добавляем строку с результатами в итоговую матрицу
            resultMatrix.add(row);
        }
        return resultMatrix;
    }

    /**
     * Умножение двух матриц.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @return Результирующая матрица, являющаяся произведением matrix1 и matrix2.
     * @throws IllegalArgumentException Если размеры матриц не соответствуют правилам умножения.
     */
    public static List<List<Integer>> multiplyMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        // Проверяем, что размеры матриц соответствуют для выполнения операции умножения
        checkMatrixMultiplication(matrix1, matrix2);
        // Создаем пустую матрицу для результата
        List<List<Integer>> resultMatrix = new ArrayList<>();
        // Проходим по каждой строке и столбцу и вычисляем соответствующие элементы
        for (int i = 0; i < matrix1.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < matrix2.get(0).size(); j++) {
                int sum = 0;
                for (int k = 0; k < matrix1.get(0).size(); k++) {
                    // Умножаем элементы из первой матрицы и второй, затем складываем
                    sum += matrix1.get(i).get(k) * matrix2.get(k).get(j);
                }
                // Добавляем результат в строку
                row.add(sum);
            }
            // Добавляем строку с результатами в итоговую матрицу
            resultMatrix.add(row);
        }
        return resultMatrix;
    }

    /**
     * Вычисление определителя квадратной матрицы.
     *
     * @param matrix Квадратная матрица.
     * @return Определитель матрицы.
     * @throws IllegalArgumentException Если матрица не квадратная.
     */
    public static int calculateDeterminant(List<List<Integer>> matrix) {
        if (matrix.size() != matrix.get(0).size()) {
            throw new IllegalArgumentException("Определитель можно найти только у квадратной матрицы");
        }
        return calculateDeterminantRecursive(matrix);
    }

    // Приватные вспомогательные методы

    /**
     * Рекурсивный метод для вычисления определителя матрицы.
     *
     * @param matrix Квадратная матрица.
     * @return Определитель матрицы.
     */
    private static int calculateDeterminantRecursive(List<List<Integer>> matrix) {
        try {
            // Получаем количество строк и столбцов в матрице
            int rows = matrix.size();
            int columns = matrix.get(0).size();
            // Если матрица имеет размер 1x1, возвращаем единственный элемент
            if (rows == 1) {
                return matrix.get(0).get(0);
            }

            // Инициализируем переменную для хранения определителя
            int determinant = 0;
            // Проходим по каждому столбцу верхней строки

            for (int j = 0; j < columns; j++) {
                // Определяем знак минора в соответствии с текущим столбцом
                int sign = (j % 2 == 0) ? 1 : -1;
                // Вычисляем минор и его определитель
                int minorDeterminant = calculateDeterminantRecursive(getMinorMatrix(matrix, j));
                // Суммируем определитель с учетом знака и элемента из верхней строки
                determinant += sign * matrix.get(0).get(j) * minorDeterminant;
            }

            return determinant;
        } catch (Exception e) {
            // В случае ошибки выбрасываем исключение с сообщением
            throw new RuntimeException("Ошибка при вычислении определителя: " + e.getMessage());
        }
    }

    /**
     * Возвращает минор матрицы, полученный удалением указанного столбца.
     *
     * @param matrix Исходная матрица.
     * @param column Индекс столбца, который необходимо исключить.
     * @return Минор матрицы после удаления столбца.
     * @throws IllegalArgumentException Возникает, если индекс столбца выходит за пределы размеров матрицы.
     */
    private static List<List<Integer>> getMinorMatrix(List<List<Integer>> matrix, int column) {
        // Создаем пустую матрицу для минора
        List<List<Integer>> minorMatrix = new ArrayList<>();
        // Получаем количество строк и столбцов в матрице
        int rows = matrix.size();
        int columns = matrix.get(0).size();

        // Проходим по каждой строке матрицы
        for (int i = 0; i < rows; i++) {
            // Исключаем строку с индексом 0
            if (i != 0) {
                // Создаем новую строку для минора
                List<Integer> minorRow = new ArrayList<>();

                // Проходим по каждому столбцу матрицы
                for (int j = 0; j < columns; j++) {
                    // Исключаем столбец с указанным индексом
                    if (j != column) {
                        // Добавляем элемент в минор
                        minorRow.add(matrix.get(i).get(j));
                    }
                }
                // Добавляем строку с элементами минора в итоговую матрицу
                minorMatrix.add(minorRow);
            }
        }
        return minorMatrix;
    }

    /**
     * Проверка соответствия размеров двух матриц для выполнения операции.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @throws IllegalArgumentException Если размеры матриц не совпадают.
     */
    private static void checkMatrixDimensions(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        if (matrix1.size() != matrix2.size() || matrix1.get(0).size() != matrix2.get(0).size()) {
            throw new IllegalArgumentException("Матрицы должны быть одного размера для выполнения операции.");
        }
    }

    /**
     * Проверка соответствия размеров матриц для выполнения умножения.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @throws IllegalArgumentException Если размеры матриц не соответствуют правилам умножения.
     */
    private static void checkMatrixMultiplication(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        int columns1 = matrix1.get(0).size();
        int rows2 = matrix2.size();
        if (columns1 != rows2) {
            throw new IllegalArgumentException("Невозможно умножить матрицы: количество столбцов первой матрицы должно равняться количеству строк второй.");
        }
    }
}
