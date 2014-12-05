package utils;

import java.util.Random;

public class Tools {
	static Random ra = new Random();

	// sigmoid函数
	public static double f(double x) {
		return 1.0 / (1.0 + Math.pow(Math.E, 0.0 - x));
	}

	// 对sigmoid求导
	public static double df(double x) {
		return f(x) * (1.0 - f(x));
	}

	public static double[][] arrayF(double[][] z) {
		double[][] a = new double[z.length][];
		for (int i = 0; i < z.length; i++) {
			a[i] = new double[z[i].length];
		}
		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[i].length; j++)
				a[i][j] = f(z[i][j]);
		}
		return a;
	}

	public static double[][] arrayDf(double[][] z) {
		double[][] a = new double[z.length][];
		for (int i = 0; i < z.length; i++) {
			a[i] = new double[z[i].length];
		}
		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[i].length; j++)
				a[i][j] = df(z[i][j]);
		}
		return a;
	}

	// 对sigmoid求导
	public static double largeDataGenerator() {
		return ra.nextGaussian();
	}

	public static double defaultDataGenerator(int size) {
		return ra.nextGaussian() / Math.sqrt(size);
	}

	public static double RandomDataGenerator() {
		return Math.random() * 2 - 1;
	}

	public static double RandomGaussianGenerator() {
		return normRand(0, 0.0001);
	}

	public static double baiseGenerator() {
		return 1.1;
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	public static double ln(double value) {
		return log(value, Math.E);
	}

	public static double normRand(double miu, double sigma2) {
		double N = 12;
		double x = 0, temp = N;
		for (int i = 0; i < N; i++)
			x = x + (Math.random());
		x = (x - temp / 2) / (Math.sqrt(temp / 12));
		x = miu + x * Math.sqrt(sigma2);
		return x;
	}

	public static void main(String[] args) {
		System.out.println(f(10));
	}

}
