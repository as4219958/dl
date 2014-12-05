package main;

import java.io.IOException;

import utils.DataGenerator;
import utils.Tools;

public class Backpropagation {
	public void forward(double[][][] activations, double[][][] zs,
			double[][] input, int layers, double[][][] w, double[][] b,
			int[] layersCount, int minibatch) {
		double[][] activation;
		double[][] z;
		activation = input;
		activations[0] = input;
		for (int i = 0; i < w.length; i++) {
			z = new double[minibatch][];
			for (int m = 0; m < minibatch; m++) {
				z[m] = new double[layersCount[i + 1]];
			}
			for (int m = 0; m < minibatch; m++) {
				for (int j = 0; j < z[m].length; j++) {
					for (int k = 0; k < activation.length; k++) {
						z[m][j] += activation[m][k] * w[i][j][k];
					}
					z[m][j] += b[i][j];
				}
			}
			zs[i] = z;
			activation = new double[minibatch][];
			for (int m = 0; m < minibatch; m++) {
				activation[m] = new double[layersCount[i + 1]];// 个数同z的个数相同,即z的个数
			}
			activation = Tools.arrayF(z);
			activations[i + 1] = activation;

		}
		// for (int i = 0; i < activations.length; i++) {
		// System.out.print("第" + i + "层:");
		// for (int j = 0; j < activations[i].length; j++)
		// System.out.print(activations[i][j] + " ");
		// System.out.println();
		// }
		// System.out.println();

	}

	public void backProp(double[][] input, double[][] target, double[][][] w,
			double[][] b, double[][][] dw, double[][] db, int layers,
			int[] layersCount, double[][][] activations, double[][][] zs,
			double[][] avgRho, boolean useSparsity, double beta, int minibatch)
			throws IOException {
		double[][] z = null;
		double[][] delta = null;
//		double[][][] ndw = new double[layers - 1][][];
//		double[][] ndb = new double[layers - 1][];
//		ndw = DataGenerator.fillWeight(ndw, "0", layersCount);
//		ndb = DataGenerator.fillBiase(ndb, "0", layersCount);
		delta = CrossEntropy.delta(activations[activations.length - 1],
		 target);
		//delta = SquaredError.delta(activations[activations.length - 1], target,
//				zs[zs.length - 1]);
		for (int n = 0; n < minibatch; n++)
			for (int i = 0; i < w[w.length - 1].length/* delta.length */; i++) {
				for (int j = 0; j < w[w.length - 1][i].length /*
															 * activations[
															 * activations
															 * .length -
															 * 2].length
															 */; j++)
					dw[w.length - 1][i][j] = delta[n][i]
							* activations[activations.length - 2][n][j];
			}
		double[] avgDelta = new double[layersCount[0]];
		for (int m = 0; m < minibatch; m++) {
			for (int j = 0; j < delta[m].length; j++) {
				avgDelta[j] += 1 / minibatch * delta[m][j];
			}

		}
		db[db.length - 1] = avgDelta;// delta的列加和
		// double[] temp = new double[layersCount[layers - 2]];// new
		// double[w[w.length-1][0].length];//倒数第二层的个数
		for (int i = w.length - 2; i >= 0; i--) {
			double[][] temp = new double[minibatch][];// new
			for (int j = 0; j < minibatch; j++) {
				temp[j] = new double[layersCount[i + 1]];
			}
			z = zs[i];
			double[][] spv = Tools.arrayDf(z);
			if (!useSparsity) {
				for (int m = 0; m < minibatch; m++) {
					for (int k = 0; k < w[i + 1][0].length; k++) {
						for (int j = 0; j < w[i + 1].length; j++) {
							temp[m][k] += w[i + 1][j][k] * delta[m][j];
						}
					}
				}
			} else {
				for (int m = 0; m < minibatch; m++) {
					for (int k = 0; k < w[i + 1][0].length; k++) {
						for (int j = 0; j < w[i + 1].length; j++) {
							temp[m][k] += w[i + 1][j][k] * delta[m][j];
						}
					}
				}
				// //////////////

				double[] sparsityParam = SparsityPenalty.calPenalty(
						avgRho[i + 1], beta);
				for (int m = 0; m < minibatch; m++) {
					for (int k = 0; k < w[i + 1][0].length; k++) {
						temp[m][k] += sparsityParam[k];
					}
				}
			}
			avgDelta = new double[layersCount[i + 1]];
			for (int m = 0; m < minibatch; m++) {
				delta[m] = new double[layersCount[i + 1]];
			}
			for (int m = 0; m < minibatch; m++) {
				for (int k = 0; k < w[i + 1][0].length; k++) {
					delta[m][k] = temp[m][k] * spv[m][k];
				}
			}
			for (int m = 0; m < minibatch; m++) {
				for (int j = 0; j < delta[m].length; j++) {
					avgDelta[j] += 1.0 / minibatch * delta[m][j];
				}

			}
			for (int m = 0; m < minibatch; m++) {
				for (int j = 0; j < w[i].length; j++) {
					for (int k = 0; k < w[i][j].length; k++) {
						dw[i][j][k] += 1.0/minibatch*delta[m][j] * activations[i][m][k];
					}
				}
			}
			db[i] = avgDelta;
		}
		// try {
		// print(ndw, "ndw"); // print(ndb, "ndb");
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// try { // print(dw, "dw");
		// print(db, "db");
		// }catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public void adjustWeight(double[][][] w, double[][] b, double[][][] dw,
			double[][] db, double eta, double lmbda, int n, int minibatch)
			throws IOException {
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				for (int k = 0; k < w[i][j].length; k++) {
					w[i][j][k] = (1 - eta * (lmbda / n)) * w[i][j][k]
							- (eta / minibatch) * dw[i][j][k];
				}
			}
		}
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				b[i][j] -= (eta / minibatch) * db[i][j];
			}
		}
	}
}
