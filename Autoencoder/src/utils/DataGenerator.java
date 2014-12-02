package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataGenerator {
	public static double[][][] fillWeight(double[][][] w, String value,
			int[] layersCount) {
		return fillWeight(w,value,layersCount,false);
	}
	public static double[][][] fillWeight(double[][][] w, String value,
			int[] layersCount, boolean tiedWeights) {
		if ("".equals(value)) {
			if (tiedWeights) {
				for (int i = 0; i < w.length / 2; i++) {
					w[i] = new double[layersCount[i + 1]][];
					for (int j = 0; j < w[i].length; j++) {
						w[i][j] = new double[layersCount[i]];
						for (int k = 0; k < w[i][j].length; k++) {
							w[i][j][k] = Tools.RandomDataGenerator();
						}
					}
				}
				for (int i = w.length - 1; i >= w.length / 2; i--) {
					w[i] = new double[layersCount[i + 1]][];
					for (int j = 0; j < w[i].length; j++) {
						w[i][j] = new double[layersCount[i]];
						for (int k = 0; k < w[i][j].length; k++) {
							w[i][j][k] = w[w.length - 1 - i][k][j];
						}
					}
				}
				
			}else{
				for (int i = 0; i < w.length ; i++) {
					w[i] = new double[layersCount[i + 1]][];
					for (int j = 0; j < w[i].length; j++) {
						w[i][j] = new double[layersCount[i]];
						for (int k = 0; k < w[i][j].length; k++) {
							w[i][j][k] = Tools.RandomDataGenerator();
						}
					}
				}
				System.out.println("123");
			}
		} else {
			double v = Double.valueOf(value);
			for (int i = 0; i < w.length; i++) {
				w[i] = new double[layersCount[i + 1]][];
				for (int j = 0; j < w[i].length; j++) {
					w[i][j] = new double[layersCount[i]];
					for (int k = 0; k < w[i][j].length; k++) {
						w[i][j][k] = v;
					}
				}
			}
		}
		return w;
	}

	// hidden
	public static double[][] fillHidden(double[][] a, int[] layersCount) {
		for (int i = 0; i < a.length; i++) {
			a[i] = new double[layersCount[i + 1]];
		}
		return a;
	}

	public static double[][] fillBiase(double[][] a, String value,
			int[] layersCount) {
		if ("".equals(value)) {
			for (int i = 0; i < a.length; i++) {
				a[i] = new double[layersCount[i + 1]];
				for (int j = 0; j < a[i].length; j++) {
					a[i][j] = Tools.RandomDataGenerator();
				}
			}
		} else {
			double v = Double.valueOf(value);
			for (int i = 0; i < a.length; i++) {
				a[i] = new double[layersCount[i + 1]];
				for (int j = 0; j < a[i].length; j++) {
					a[i][j] = v;
				}
			}
		}
		return a;
	}

	public static double[][] loadValues(String filename, int rnum,
			double[][] array, int rows) throws IOException {

		BufferedReader br = Files.newBufferedReader(Paths.get(filename),
				Charset.forName("utf-8"));
		String str = "";
		int lineNum = 0;
		array = new double[rows][];
		while ((str = br.readLine()) != null) {
			if (lineNum < rnum) {
				lineNum++;
				continue;
			}
			if (lineNum > rnum + rows - 1)
				break;
			String values[] = str.split(",");
			array[lineNum - rnum] = new double[values.length];
			for (int j = 0; j < values.length; j++) {
				array[lineNum - rnum][j] = Double.valueOf(values[j]);
			}
			lineNum++;
		}
		return array;

		// String str = "";
		// int fileLine = 0;
		// int count = 0;
		// while (ra.readLine() != null) {
		// fileLine++;
		// }
		// double a[][] = new double[fileLine][];
		// ra.seek(0);
		// while ((str = ra.readLine()) != null) {
		// String values[] = str.split(",");
		//
		// double b[] = new double[values.length];
		// for (int i = 0; i < values.length; i++) {
		// b[i] = Double.valueOf(values[i]);
		// }
		// a[count] = b;
		// count++;
		// }
		// return a;
	}
}
