package prepare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class GenerateFaceImgFiles implements GenerateFilesInterface{
	final int N = 1;
	final String path = "C:/Users/Administrator/Desktop/FERET_80_80";
	@Override
	public void generateFile(String dist, int start, int len) throws IOException {
		int n = 0;
		File directory = new File(path);
		File[] childDirectories = directory.listFiles();
		FileOutputStream f = new FileOutputStream(dist);// true表示添加
		FileChannel fc = f.getChannel();// 用getChannel()方法转化为FileChannel类
		for (File child : childDirectories) {
			if (n++ < N) {
				List<Image> imgList = Image.getImages(
						child.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.endsWith(".tif");
							}
						}), start, len);

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
		}
		fc.close();
		f.close();
	}
}
