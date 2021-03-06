import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;

public class Gauss {

	public static void main(String[] args) throws Exception {

		Scanner keyboard = new Scanner(System.in);

		int n = 0;
		double[][] coefArray = null;

		int option = options();
		if (option == 1) {
			n = numEquations();
			coefArray = initializeMatrix(n);
		} else if (option == 2) {
			System.out.print("Enter file name: ");
			String filename = keyboard.nextLine();
			coefArray = openFile(filename);
			n = coefArray.length;
		}

		gaussElimination(n, coefArray);

	}

	public static int options() {

		Scanner keyboard = new Scanner(System.in);

		System.out.println("Press 1 for command line input");
		System.out.println("Press 2 to get input from file");

		int option = keyboard.nextInt();
		return option;

	}

	public static double[][] openFile(String file) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;

		while (reader.readLine() != null)
			lines++;
		reader.close();
		System.out.println(lines);

		int rows = lines;
		int cols = lines + 1;
		double[][] coef = new double[rows][cols];

		reader = new BufferedReader(new FileReader(file));

		for (int i = 0; i < rows; i++) {
			String[] line = reader.readLine().trim().split(" ");
			for (int j = 0; j < cols; j++) {
				coef[i][j] = Double.parseDouble(line[j]);
				System.out.print(coef[i][j] + " ");
			}
			System.out.println();
		}

		return coef;
	}

	public static void gaussElimination(int n, double[][] matrix) {

		NumberFormat formatter = new DecimalFormat("#0.00");

		double[] scale = new double[n];
		double[] ratio = new double[n];
		double max = 0, multiple;
		int pivotRow = 0, iteration = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Math.abs(matrix[i][j]) > max)
					max = Math.abs(matrix[i][j]);
			}

			scale[i] = max;
			max = 0;
		}

		while (iteration < n - 1) {

			for (int i = 0; i < n; i++) {
				if (ratio[i] != -1)
					ratio[i] = Math.abs(matrix[i][iteration] / scale[i]);
				if (ratio[i] > max) {
					max = ratio[i];
					pivotRow = i + 1;
				}
			}
			max = 0;

			System.out.print("Scaled ratios = ");
			for (int i = 0; i < n; i++)
				System.out.print(formatter.format(ratio[i]) + " ");
			System.out.println();
			System.out.println("Equation with highest scaled pivot - " + pivotRow);
			System.out.println();

			for (int i = 0; i < n; i++) {
				if (i != pivotRow - 1 && ratio[i] != -1) {
					multiple = Math.abs(matrix[i][iteration] / matrix[pivotRow - 1][iteration]);
					if ((matrix[i][iteration] > 0 && matrix[pivotRow - 1][iteration] > 0)
							|| (matrix[i][iteration] < 0 && matrix[pivotRow - 1][iteration] < 0))
						multiple *= -1;
					for (int j = 0; j < n + 1; j++) {
						matrix[i][j] = (matrix[pivotRow - 1][j] * multiple) + matrix[i][j];
					}
				}
			}

			ratio[pivotRow - 1] = -1;

			iteration++;
		}

		formatMatrix(n, matrix);

	}

	public static int numEquations() {

		Scanner keyboard = new Scanner(System.in);

		System.out.print("Enter num of equations: n = ");
		int n = keyboard.nextInt();

		return n;
	}

	public static double[][] initializeMatrix(int n) {

		Scanner keyboard = new Scanner(System.in);
		double[][] matrix = new double[n][n + 1];

		System.out.println("Enter all " + n + " rows of coefficient matrix plus the b value:");

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n + 1; j++) {
				matrix[i][j] = keyboard.nextDouble();
			}
			System.out.println();
		}

		return matrix;
	}

	public static void formatMatrix(int n, double[][] matrix) {

		double[][] mat = new double[n][n + 1];
		int moves = 0;
		int iteration = 0;
		int var = 1;

		while (moves != n) {
			if (matrix[iteration][0] != 0) {
				for (int j = 0; j < n + 1; j++)
					mat[0][j] = matrix[iteration][j];
			} else if (matrix[iteration][var] != 0 && matrix[iteration][var - 1] == 0) {
				for (int j = 0; j < n + 1; j++)
					mat[var][j] = matrix[iteration][j];
				var++;
			}

			iteration++;
			moves++;

		}

		print(n, mat);
		solveMatrix(n, mat);

	}

	public static void solveMatrix(int n, double[][] matrix) {

		NumberFormat formatter = new DecimalFormat("#0.00");
		int equation = n - 1;
		double[] xvals = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double sum = 0;

		while (equation >= 0) {
			for (int j = 0; j < n; j++) {
				if (j != equation)
					sum += xvals[j] * matrix[equation][j];
			}
			double val = matrix[equation][n] - sum;
			xvals[equation] = val / matrix[equation][equation];

			sum = 0;
			equation--;
		}

		int i = n - 1;
		int val = 1;
		while (i >= 0) {
			if (xvals[i] % 1 != 0)
				System.out.println("X" + val + " = " + formatter.format(xvals[i]));
			else
				System.out.println("X" + val + " = " + Math.round(xvals[i]));
			i--;
			val++;
		}

	}

	public static void print(int n, double[][] matrix) {

		NumberFormat formatter = new DecimalFormat("#0.00");

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n + 1; j++) {
				if (matrix[i][j] % 1 != 0)
					System.out.print(formatter.format(matrix[i][j]) + "\t");
				else
					System.out.print((int) matrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}

}
