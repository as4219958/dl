package main;

import utils.Tools;

public class CrossEntropy {
	public static double cost(double output[][], double target[][], double[][][] batchActivations,int minibatch,double beta) {
		return cost(output, target,batchActivations, minibatch,beta, false);
	}

	public static double cost(double output[][], double target[][],double[][][] batchActivations,
			int minibatch,double beta, boolean useSparsity) {
		double sum = 0;
		int n = output.length;
		if (useSparsity) {
			for (int m = 0; m < n; m++) {
				for (int i = 0; i < n; i++) {
					sum +=-1.0 / minibatch*( target[m][i] * Tools.ln(output[m][i])
							+ (1 - target[m][i]) * Tools.ln(1 - output[m][i]))+SparsityPenalty.cost(batchActivations, minibatch);
				}
			}
		} else {
			for (int m = 0; m < n; m++) {
				for (int i = 0; i < n; i++) {
					sum += -1.0 / minibatch*(target[m][i] * Tools.ln(output[m][i])
							+ (1 - target[m][i]) * Tools.ln(1 - output[m][i]));
				}
			}
		}
		return   sum;
	}

	public static double[][] delta(double[][] output, double[][] target) {
		int minibatch = output.length;
		double[][] delta = new double[minibatch][];
		for (int i = 0; i < minibatch; i++) {
			delta[i] = new double[output[i].length];
		}
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output[i].length; j++) {
				delta[i][j] = (output[i][j] - target[i][j]);
			}
		}
		return delta;
	}
}
