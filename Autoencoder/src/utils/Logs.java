package utils;

import java.io.FileWriter;
import java.io.IOException;

import main.Startup;

public class Logs {
	public static void print(double[][][] w, String str) throws IOException {

		FileWriter fw = new FileWriter(Startup.class.getResource("/")
				.toString().substring(6)
				+ "logs.txt", false);
		fw.write(str + ":\r\n");
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				for (int k = 0; k < w[i][j].length; k++) {
					fw.write(w[i][j][k] + " ");
				}
				fw.write("\r\n");
			}
			fw.write("\r\n");
		}
		fw.write("\r\n");
		fw.write("\r\n");
		fw.close();
	}

	public static void print(double[][] b, String str) throws IOException {
		FileWriter fw = new FileWriter(Startup.class.getResource("/")
				.toString().substring(6)
				+ "logs.txt", true);
		fw.write(str + ":\r\n");
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				fw.write(b[i][j] + " ");
			}
			fw.write("\r\n");
		}
		fw.write("\r\n");
		fw.write("\r\n");
		fw.close();
	}
}
