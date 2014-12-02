package prepare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

public class Image {
	public int height;
	public int width;
	public double[][] pixels;

	public Image(int h, int w, double[][] ps) {
		this.height = h;
		this.width = w;
		pixels = ps;
	}

	public static List<Image> getImages(File[] files)throws IOException {
		return getImages(files,0,files.length);
	}
	public static List<Image> getImages(File[] files, int start, int len)
			throws IOException {
		int h, w;
		double[][] pixels;
		Image[] images =  new Image[len];
		for (int i = start; i < start + len; i++) {
			BufferedImage bimg = ImageIO.read(files[i]);
			h = bimg.getHeight();
			w = bimg.getWidth();
			pixels = new double[h][];
			for (int j = 0; j < pixels.length; j++) {
				pixels[j] = new double[w];
			}
			for (int j = 0; j < h; j++) {
				for (int k = 0; k < w; k++) {
					int rgb = bimg.getRGB(k, j);
					int r = (rgb & 0xff0000);
					int g = (rgb & 0xff00);
					int b = (rgb & 0xff);
					pixels[j][k] = (double)(r + g + b) / imageTools.MAX_RGB;
				}
				images[i - start] = new Image(h, w, pixels);
			}
		}
		/*
		 * for (int i = start; i < start+len; i++) { BufferedImage bimg =
		 * ImageIO.read(files[i]); h = bimg.getHeight(); w = bimg.getWidth();
		 * pixels = new int[h*w]; // for(int j=0;j<pixels.length;j++){ //
		 * pixels[j]=new double[w]; // } bimg.getRGB(0, 0, w, h, pixels, 0, w);
		 * images[i-start] = new Image(h, w, pixels); // for(int j=0; j<h; j++)
		 * { // for(int k=0; k<w; k++) { // int rgb = bimg.getRGB(k, j); // int
		 * r = (rgb & 0xff0000) >> 16; // int g = (rgb & 0xff00) >> 8; // int b
		 * = (rgb & 0xff); // pixels[j][k] =(double)(r * 0.3 + g * 0.59 + b *
		 * 0.11)/255; //º∆À„ª“∂»÷µ // } // images[i-start] = new Image(h, w, pixels);
		 * // } }
		 */
		return Arrays.asList(images);
	}

	
}
