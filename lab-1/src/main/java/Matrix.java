import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Matrix {

    public Matrix(List<List<Double>> values) {
        this(values, System.out);
    }

    public Matrix(List<List<Double>> values, PrintStream out) throws IllegalArgumentException {
        int n = values.size();
        for (List<Double> row : values) {
            if (row.size() != n + 1) {
                throw new IllegalArgumentException("The matrix must be square");
            }
        }

        this.values = values;
        this.printer = new MatrixPrinter(out);
    }

    public int getN() {
        return values.size();
    }

    public double calcDeterminant() {
        List<List<Double>> squaredMatrix = new ArrayList<>(getN());
        for (int row = 0; row < getN(); ++row) {
            squaredMatrix.add(new ArrayList<>(getN()));
            for (int col = 0; col < getN(); ++col) {
                squaredMatrix.get(row).add(values.get(row).get(col));
            }
        }
        return calcDeterminantImpl(squaredMatrix);
    }

    private static double calcDeterminantImpl(List<List<Double>> matrix) {
        double D = 0;

        if (matrix.size() == 1) {
            return matrix.get(0).get(0);
        }

        int sign = 1;

        // Iterate through first row
        for (int i = 0; i < matrix.size(); ++i) {
            List<List<Double>> temp = getCofactor(matrix, 0, i);
            D += sign * matrix.get(0).get(i) * calcDeterminantImpl(temp);

            sign = -sign;
        }

        return D;
    }

    public List<Double> calcResults() {
        List<List<Double>> tempValues = values;
        List<Double> results = new ArrayList<>(getN());

        // Прямой ход
        for (int row = 0; row < getN(); ++row) {
            double lead = values.get(row).get(row);
            List<Double> equation = makeEquation(tempValues, lead, row);
            bottomSubtract(tempValues, row, equation);
        }

        // Обратный ход
        for (int col = getN(); col >= 0; --col) {
            double lastValue = tempValues.get(getN()-1).get(col);
            results.add(findResult(tempValues, col, lastValue, results));
        }

        return results;
    }

    private double findResult(List<List<Double>> matrix, int col, double initValue, List<Double> prevResults) {
        double value = initValue;
        for (int row = matrix.size()-2; row >= 0; --row) {
            value -= matrix.get(row).get(col) * prevResults.get(row);
        }
        return value;
    }

    private static List<Double> makeEquation(List<List<Double>> matrix, double lead, int col) {
        List<Double> equation = new ArrayList<>(matrix.size());
        for (List<Double> row : matrix) {
            double value = row.get(col) / lead;
            equation.add(value);
            row.set(col, value);
        }
        return equation;
    }

    private static void bottomSubtract(List<List<Double>> matrix, int row, List<Double> equation) {
        for (int rowIndex = row + 1; rowIndex < matrix.size(); ++rowIndex) {
            double nextLead = matrix.get(row).get(rowIndex);
            for (int colIndex = 0; colIndex < matrix.size(); ++colIndex) {
                double value = matrix.get(rowIndex).get(colIndex) - nextLead * equation.get(colIndex);
            }
        }
    }


    public List<Double> getResults() {
        if (results == null) {
            results = calcResults();
        }
        return results;
    }

    public List<List<Double>> getValues() {
        return values;
    }

    public ArrayList<ArrayList<Double>> getTriangularMatrix() {
        return null;
    }

    class MatrixPrinter {
        public MatrixPrinter(PrintStream out) {
            this(out, ",\t");
        }

        public MatrixPrinter(PrintStream out, String divider) {
            this.out = out;
            this.divider = divider;
        }

        public void printMatrix() {
            out.println("Values:");
            for (List<Double> row : getValues()) {
                out.print(row.get(0));
                for (int i = 1; i < row.size(); ++i) {
                    out.print(divider);
                    out.print(row.get(i));
                }
                out.println();
            }
        }

        public void printDeterminant() {
            out.printf("D = %s%n", calcDeterminant());
        }

        public void printTriangularMatrix() {
            List<ArrayList<Double>> triangularMatrix = getTriangularMatrix();
            for (List<Double> row : triangularMatrix) {
                out.print(row.get(0));
                for (int i = 1; i < row.size(); ++i) {
                    out.print(divider);
                    out.print(row.get(i));
                }
                out.println();
            }
        }

        public void printResultColumn() {
            List<Double> resultColumn = getResults();
            for (double res : resultColumn) {
                out.println(res);
            }
        }

        public void printErrors() {
            // Implement
        }

        private final PrintStream out;
        private final String divider;
    }

    private static List<List<Double>> getCofactor(List<List<Double>> values, int p, int q) {
        List<List<Double>> cofactor = new ArrayList<>();

        int i = 0;
        for (int row = 0; row < values.size(); ++row) {
            if (row == p) { // skip row
                continue;
            }
            cofactor.add(new ArrayList<>());
            for (int col = 0; col < values.size(); ++col) {
                if (col != q) { // skip column
                    cofactor.get(i).add(values.get(row).get(col));
                }
            }
            i++;
        }
        return cofactor;
    }

    public MatrixPrinter printer;

    private final List<List<Double>> values;
    private List<Double> results = null;
}
