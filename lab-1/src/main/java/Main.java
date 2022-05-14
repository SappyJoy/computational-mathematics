import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        // Реализовать ввод из файла
        // Добавить флаги -f [файл] и -r - рандом

        List<List<Double>> values = new ArrayList<>(n);

        // a_0 * x_0 + a_1 * x_1 + ... = r
        for (int row = 0; row < n; ++row) {
            values.add(new ArrayList<>());
            for (int col = 0; col < n + 1; ++col) {
                double a = in.nextDouble();
                values.get(row).add(a);
            }
        }

        Matrix matrix = new Matrix(values);

        matrix.printer.printMatrix();
        System.out.println();

        matrix.printer.printDeterminant();
        System.out.println();

        matrix.printer.printResultColumn();
        System.out.println();

        matrix.printer.printErrors();
    }
}
