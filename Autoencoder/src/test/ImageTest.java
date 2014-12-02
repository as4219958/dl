package test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import prepare.Image;

public class ImageTest {
	public static void main(String[] args) throws IOException {
		File directory = new File("C:/Users/Administrator/Desktop/FERET_80_80");
		File[] childDirectories = directory.listFiles();
		for(File child:childDirectories){
			List<Image> list = Image.getImages(child.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					  return name.endsWith(".tif"); 
				}
			}));
		}
	}

}
