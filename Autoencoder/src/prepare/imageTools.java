package prepare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class imageTools {
	public static final int MAX_RGB = 16777215;

	public static void dataToImage(double[] image, String path, int height,
			int width) throws IOException {

		BufferedImage bimg = new BufferedImage(height, width,
				BufferedImage.TYPE_BYTE_GRAY);
		bimg.setRGB(0, 0, width, height, double2Int(image)/*double2Int(Metrix2Array(image))*/, 0, width);
		ImageIO.write(bimg, "jpg", new File(path));
	}

	public static double[] Metrix2Array(double[][] metrix) {
		double array[] = null;
		int width = 0, height = 0;
		if (metrix != null && metrix[0] != null) {
			width = metrix[0].length;
			height = metrix.length;
			array = new double[width * height];
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array[i * height + j] = metrix[i][j];
			}
		}
		return array;
	}

	public static int[] double2Int(double[] array) {
		int[] argbArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			argbArray[i] = (int) (array[i] * MAX_RGB) + (0xff << 24);
		}
		return argbArray;
	}
}
