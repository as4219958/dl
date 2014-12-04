package main;

import utils.Tools;

public class SquaredError {
	public static double cost(double output[][], double target[][]) {
		double sum = 0;
		int n = output.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < output[i].length; j++) {
				sum += Math.pow(target[i][j] - output[i][j], 2);
			}
		}
		return 1.0 / n * sum;
	}

	public static double[][] delta(double[][] output, double[][] target,
			double[][] z) {
		int minibatch = output.length;
		double[][] delta = new double[minibatch][];
		for (int i = 0; i < minibatch; i++) {
			delta[i] = new double[output[i].length];
		}
		double[][] dz = Tools.arrayDf(z);
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output[i].length; j++) {
				delta[i][j] = (output[i][j] - target[i][j]) * dz[i][j];
			}
		}
		return delta;
	}
}
