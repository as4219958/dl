package main;

import utils.Tools;

class SparsityPenalty {
	static final double RHO = 0.01;

	public static double cost(double[][][] batchActivations, int minibatch) {
		double[][] avgRho = calAvgRho(batchActivations, minibatch);
		double sum = 0;
		for (int i = 1; i < avgRho.length - 1; i++) {
			for (int j = 0; i < avgRho[0].length; j++) {
				sum += RHO * Tools.ln(RHO / avgRho[i][j]) + RHO
						* Tools.ln((1 - RHO) / (1 - avgRho[i][j]));
			}
		}
		return sum;
	}

	public static double[][] calAvgRho(double[][][] batchActivations,
			int minibatch) {
		double[][] avgRho = new double[batchActivations.length][];
		for (int i = 0; i < avgRho.length; i++) {
			avgRho[i] = new double[batchActivations[i][0].length];

		}
		for (int i = 0; i < batchActivations.length; i++) {
			for (int m = 0; m < minibatch; m++) {
				for (int j = 0; j < batchActivations[i][m].length; j++) {
					avgRho[i][j] += 1.0 / minibatch * batchActivations[i][m][j];
				}
			}
		}
		return avgRho;
	}

	public static double[] calPenalty(double[] avgRho, double beta) {
		int len = avgRho.length;
		double[] params = new double[len];
		for (int i = 0; i < len; i++) {
			params[i] = beta * (-RHO / avgRho[i] + (1 - RHO) / (1 - avgRho[i]));
		}
		return params;
	}
}