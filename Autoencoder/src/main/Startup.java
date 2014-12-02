package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

import prepare.LoadFiles;
import prepare.imageTools;
import utils.DataGenerator;
import utils.GenerateChart;

public class Startup {
	int cunt = 0;
	final String inputTxt = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "input.txt";
	final String targetTxt = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "target.txt";
	final String validationTxt = LoadFiles.class.getResource("/")
			.toString().substring(6)
			+ "evaluation.txt";
	final String testTxt = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "test.txt";

	private double input[][];
	private double evaluation[][];
	private double test[][];
	// private double target[][];
	private double w[][][];// 输入隐层权值
	private double lmbda;//
	private double eta;// 学习率
	private double beta;
	private double hidden[][];
	private int layersCount[];
	private int trainTimes;
	private int layers;// 层数
	private int minibatch;
	private double[][] b;
	private int n;

	private double[][][] dw;
	private double[][] db;

	private boolean useSparsity = false;

	public void setUseSparsity(boolean useSparsity) {
		this.useSparsity = useSparsity;
	}

	private void init(String str) throws IOException {
		Properties pps = new Properties();
		pps.load(Startup.class.getClassLoader().getResourceAsStream(
				"init.properties"));
		String wStr = pps.getProperty("w");
		String lmbdaStr = pps.getProperty("lmbda");
		String etaStr = pps.getProperty("eta");
		String betaStr = pps.getProperty("beta");
		String[] layersCountStr = pps.getProperty("layersCount").split(",");
		String trainTimesStr = pps.getProperty("trainTimes");
		minibatch = Integer.valueOf(pps.getProperty("minibatch"));
		n = Integer.valueOf(pps.getProperty("n"));
		layersCount = new int[layersCountStr.length];
		for (int i = 0; i < layersCountStr.length; i++) {
			layersCount[i] = Integer.valueOf(layersCountStr[i]);
		}
		layers = layersCount.length;
		hidden = new double[layers - 2][];
		b = new double[layers - 1][];
		trainTimes = Integer.valueOf(trainTimesStr);
		w = new double[layers - 1][][];
		dw = new double[w.length][][];
		db = new double[b.length][];
		eta = Double.valueOf(etaStr);
		lmbda = Double.valueOf(lmbdaStr);
		beta = Double.valueOf(betaStr);
		if ("train".equals(str)) {
			DataGenerator.fillHidden(hidden, layersCount);
			DataGenerator.fillBiase(b, "", layersCount);
			DataGenerator.fillWeight(w, wStr, layersCount);
		}
		if ("test".equals(str)) {
			DataGenerator.fillHidden(hidden, layersCount);
			DataGenerator.fillBiase(b, "0", layersCount);
			DataGenerator.fillWeight(w, "0", layersCount);
		}
	}

	public void train(Backpropagation bp) throws IOException {
		Random r = new Random();
		int rnum;
		double[][] activations = new double[layers][];
		double[][] zs = new double[layers - 1][];
		double[][] batchActivations = new double[layers][];
		double[][] avgRho = new double[layers][];
		for (int i = 0; i < layers; i++) {
			batchActivations[i] = new double[layersCount[i]];
			avgRho[i] = new double[layersCount[i]];
		}
		for (int i = 0; i < trainTimes; i++) {
			if (useSparsity) {
				for (int m = 0; m < layers; m++) {
					Arrays.fill(batchActivations[m], 0);
				}
			}
			rnum = r.nextInt(n) / minibatch * minibatch;
			dw = DataGenerator.fillWeight(dw, "0", layersCount);
			db = DataGenerator.fillBiase(db, "0", layersCount);
			input = DataGenerator
					.loadValues(inputTxt, rnum, input, minibatch);
			System.out.println("rnum:" + rnum);
			for (int j = 0; j < minibatch; j++) {
				bp.forward(activations, zs, input[j], layers, w, b, layersCount);
				if (useSparsity) {
					for (int m = 0; m < layers; m++) {
						for (int n = 0; n < batchActivations[m].length; n++) {
							batchActivations[m][n] += activations[m][n];
						}
					}
				}
				// //
				bp.backProp(input[j], input[j], w, b, dw, db, layers,
						layersCount, activations, zs, avgRho, useSparsity, beta);
			}
			if (useSparsity) {
				for (int m = 0; m < layers; m++) {
					avgRho[m] = SparsityPenalty.calAvgRho(batchActivations[m],
							avgRho[m], minibatch);
				}
			}
			bp.adjustWeight(w, b, dw, db, eta, lmbda, n, minibatch);
			GenerateChart
					.createDataset(accuracy(bp, avgRho), String.valueOf(i));
		}
		// System.out.print(accuary()+" ");
	}

	private double accuracy(Backpropagation bp, double[][] avgRho)
			throws IOException {
		double[][] activations = new double[layers][];
		double[][] zs = new double[layers - 1][];
		Random r = new Random();
//		int rnum = r.nextInt(n) / 3 * 2 / minibatch * minibatch;
//		evaluation = DataGenerator.loadValues(validationTxt, rnum, evaluation,
//				minibatch);
		int rnum = r.nextInt(n)/ minibatch * minibatch;
		evaluation = DataGenerator.loadValues(inputTxt, 1, evaluation,
				minibatch);
		double sum = 0;
		for (int j = 0; j < minibatch; j++) {
			bp.forward(activations, zs, evaluation[j], layers, w, b,
					layersCount);
			double[] activation = activations[activations.length - 1];
			// double[] a = regulate(activation);

			 sum += SquaredError.cost(activation, evaluation[j]);
			 //sum += CrossEntropy.cost(activation, evaluation[j], minibatch,useSparsity);
		}

		return sum;

	}

	private double[] regulate(double[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = Math.round(a[i]);
		}
		return a;
	}

	private void writeBack() throws IOException {
		FileWriter fw = new FileWriter(Startup.class.getResource("/")
				.toString().substring(6)
				+ "output.txt", false);
		fw.write("w" + "\r\n");
		fw.write("p0" + "\r\n");
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				for (int k = 0; k < w[i][j].length; k++) {
					fw.write(String.valueOf(w[i][j][k]));
					if (k != w[i][j].length - 1) {
						fw.write(",");
					}
				}
				fw.write("\r\n");
			}
			if (i != w.length - 1) {
				fw.write("p" + (i + 1) + "\r\n");
			}
		}
		fw.write("b" + "\r\n");
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				fw.write(String.valueOf(b[i][j]));
				if (j != b[i].length - 1) {
					fw.write(",");
				}
			}
			fw.write("\r\n");
		}
		fw.close();
	}

	private void loadParameters() throws IOException {
		FileReader fw = new FileReader(Startup.class.getResource("/")
				.toString().substring(6)
				+ "output.txt");
		BufferedReader br = new BufferedReader(fw);
		String s = "";
		String[] str;
		int i = 0;
		int j = 0;
		boolean bool = false;
		while ((s = br.readLine()) != null && !"b".equals(s)) {
			if (bool == false && "w".equals(s)) {
				bool = true;
				continue;
			}
			if (bool == false) {
				System.out.println("读取数据出错了!");
				System.exit(1);
			}
			if (s.startsWith("p")) {
				i = Integer.valueOf(s.substring(1));
				j = 0;
				continue;
			}
			if ("b".equals(s))
				break;
			else {
				str = s.split(",");
				for (int k = 0; k < w[i][j].length; k++) {
					w[i][j][k] = Double.valueOf(str[k]);
				}
			}
			j++;
		}
		if (!"b".equals(s)) {
			System.out.println("读取数据出错了!");
			System.exit(1);
		}
		j = 0;
		while ((s = br.readLine()) != null) {
			str = s.split(",");
			for (int k = 0; k < b[j].length; k++) {
				b[j][k] = Double.valueOf(str[k]);
			}
			j++;
		}
		// try {
		// print(w, "w");
		// print(b, "b");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public void test(Backpropagation bp) throws IOException {
		// double[][] image;double2Int(
		double[] image;
		double[][] activations = new double[layers][];
		double[][] zs = new double[layers - 1][];
		// image = new double[minibatch][];
		Random r = new Random();
//		int rnum = r.nextInt(n / 3 * 2) / minibatch * minibatch;
//		System.out.print(rnum / 2.0);
//		test = DataGenerator.loadValues(testTxt, rnum, test, 1/* minibatch */);
		int rnum = r.nextInt(n);
		System.out.print(rnum/10.0);
		test = DataGenerator.loadValues(inputTxt, 1, test, 1/* minibatch */);
		for (int i = 0; i < test.length/* minibatch */; i++) {
			bp.forward(activations, zs, test[i], layers, w, b, layersCount);
			image = activations[activations.length - 1];
			imageTools.dataToImage(image, "D:/bc.jpg", 28, 28);// minibatch,
																// layersCount[0]);
		}
	}

	public static void main(String[] args) throws IOException {
		Startup su = new Startup();
		su.setUseSparsity(false);
		Backpropagation bp = new Backpropagation();
		GenerateChart chart = new GenerateChart("achart");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);

		if ("train".equals(args[0])) {
			su.init("train");
			// su.print(su.w,"w");
			// su.print(su.b,"b");
			su.train(bp);
			su.writeBack();
		}

		if ("test".equals(args[0])) {
			su.init("test");
			su.loadParameters();
			su.test(bp);
		}

	}
}
