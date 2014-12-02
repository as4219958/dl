package prepare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GenerateMnistImgFiles implements GenerateFilesInterface {
	final int COUNT = 10;
	final String path = "C:/Users/Administrator/Desktop/t10k-images";

	@Override
	public void generateFile(String dist, int start, int len)
			throws IOException {
		File directory = new File(path);
		FileOutputStream f = new FileOutputStream(dist);// true表示添加
		FileChannel fc = f.getChannel();// 用getChannel()方法转化为FileChannel类
		for (int i = 0; i <= 9; i++) {
			List<Image> imgList = Image.getImages(getRandomImg(directory, i,
					COUNT));

			for (Image img : imgList) {
				StringBuilder pixs = new StringBuilder();
				for (double[] pixLine : img.pixels) {
					for (double pix : pixLine) {
						pixs.append(pix + ",");
					}
				}
				pixs.deleteCharAt(pixs.length() - 1);
				pixs.append("\r\n");
				fc.write(ByteBuffer.wrap(pixs.toString().getBytes()));
			}
		}
		fc.close();
		f.close();
	}

	private File[] getRandomImg(File directory, final int num, final int count) {
		File[] files = new File[count];
		List<File> imgList = Arrays.asList(directory
				.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".bmp")
								&& name.startsWith(num + "_");
					}
				}));
		int fileLen = imgList.size();
		List<File> subImgList = new LinkedList<File>();
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			subImgList.add(imgList.get(r.nextInt(fileLen)));
		}
		return subImgList.toArray(files);
	}

}
