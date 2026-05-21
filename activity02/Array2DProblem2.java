package activity2;

import java.util.*;

public class Array2DProblem2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double[][] matrix = new double[4][4];

        System.out.println("Enter a 4-by-4 matrix row by row:");

        // Read the matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = input.nextDouble();
            }
        }

        // Display the sum of the major diagonal
        System.out.println(
                "Sum of the elements in the major diagonal is " + sumMajorDiagonal(matrix));

        input.close();
    }

    // Method to sum the major diagonal
    public static double sumMajorDiagonal(double[][] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i][i];
        }
        return sum;
    }

}
