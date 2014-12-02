package main;

import java.io.IOException;

import utils.DataGenerator;
import utils.Tools;

public class Backpropagation {
	public void forward(double[][] activations, double[][] zs, double[] input,
			int layers, double[][][] w, double[][] b, int[] layersCount) {
		double[] activation;
		double[] z;
		activation = input;
		activations[0] = input;
		for (int i = 0; i < w.length; i++) {
			z = new double[layersCount[i + 1]];
			for (int j = 0; j < z.length; j++) {
				for (int k = 0; k < activation.length; k++) {
					z[j] += activation[k] * w[i][j][k];
				}
				z[j] += b[i][j];
			}
			zs[i] = z;
			activation = new double[layersCount[i + 1]];// ����ͬz�ĸ�����ͬ,��z�ĸ���
			activation = Tools.arrayF(z);
			activations[i + 1] = activation;
		}
		// for (int i = 0; i < activations.length; i++) {
		// System.out.print("��" + i + "��:");
		// for (int j = 0; j < activations[i].length; j++)
		// System.out.print(activations[i][j] + " ");
		// System.out.println();
		// }
		// System.out.println();

	}

	public void backProp(double[] input, double[] target, double[][][] w,
			double[][] b, double[][][] dw, double[][] db, int layers,
			int[] layersCount, double[][] activations, double[][] zs,
			double[][] avgRho, boolean useSparsity, double beta)
			throws IOException {
		double[] z = null;
		double[] delta = null;
		double[][][] ndw = new double[layers - 1][][];
		double[][] ndb = new double[layers - 1][];
		ndw = DataGenerator.fillWeight(ndw, "0", layersCount);
		ndb = DataGenerator.fillBiase(ndb, "0", layersCount);
		//delta = CrossEntropy.delta(activations[activations.length - 1], target);
		 delta = SquaredError.delta(activations[activations.length - 1],
		 target,zs[zs.length-1]);
		for (int i = 0; i < w[w.length - 1].length/* delta.length */; i++) {
			for (int j = 0; j < w[w.length - 1][i].length /*
														 * activations[activations
														 * .length - 2].length
														 */; j++)
				ndw[w.length - 1][i][j] = delta[i]
						* activations[activations.length - 2][j];
		}
		ndb[db.length - 1] = delta;
		// double[] temp = new double[layersCount[layers - 2]];// new
		// double[w[w.length-1][0].length];//�����ڶ���ĸ���
		for (int i = w.length - 2; i >= 0; i--) {
			double[] temp = new double[layersCount[i + 1]];// new
			z = zs[i];
			double[] spv = Tools.arrayDf(z);
			if (!useSparsity) {
				for (int k = 0; k < w[i + 1][0].length; k++) {
					for (int j = 0; j < w[i + 1].length; j++) {
						temp[k] += w[i + 1][j][k] * delta[j];
					}
				}
			} else {
				for (int k = 0; k < w[i + 1][0].length; k++) {
					for (int j = 0; j < w[i + 1].length; j++) {
						temp[k] += w[i + 1][j][k] * delta[j];
					}
				}
				// //////////////

				double[] sparsityParam = SparsityPenalty.calPenalty(avgRho[i + 1],beta);
				for (int k = 0; k < w[i + 1][0].length; k++) {
					temp[k] += sparsityParam[k];
				}
			}
			delta = new double[layersCount[i + 1]];
			for (int k = 0; k < w[i + 1][0].length; k++) {
				delta[k] = temp[k] * spv[k];
			}
			for (int j = 0; j < w[i].length; j++) {
				for (int k = 0; k < w[i][j].length; k++) {
					ndw[i][j][k] = delta[j] * activations[i][k];
				}
			}
			ndb[i] = delta;
		}
		// try { 
		// print(ndw, "ndw"); // print(ndb, "ndb"); 
		// } catch (IOException e1) { 
		// e1.printStackTrace(); 
		// }
		// try { // print(dw, "dw"); 
		// print(db, "db"); 
		// }catch (IOException e) { 
		//e.printStackTrace(); 
		// }
		for (int i = 0; i < dw.length; i++) {
			for (int j = 0; j < dw[i].length; j++) {
				for (int k = 0; k < dw[i][j].length; k++) {
					dw[i][j][k] += ndw[i][j][k];
				}
			}
		}

		for (int i = 0; i < db.length; i++) {
			for (int j = 0; j < db[i].length; j++) {
				db[i][j] += ndb[i][j];
			}
		}
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