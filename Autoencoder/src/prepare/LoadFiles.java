package prepare;

import java.io.IOException;

public class LoadFiles {
	final String inputDist = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "input.txt";
	final String targetDist = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "target.txt";
	final String validationDist = LoadFiles.class.getResource("/")
			.toString().substring(6)
			+ "evaluation.txt";
	final String testDist = LoadFiles.class.getResource("/").toString()
			.substring(6)
			+ "test.txt";

	private void generateInput(GenerateFilesInterface gfi) throws IOException {
		gfi.generateFile(inputDist, 0, 3);
	}

	private void generateTarget(GenerateFilesInterface gfi) throws IOException {
		gfi.generateFile(targetDist, 0, 3);
	}

	private void generateValidation(GenerateFilesInterface gfi) throws IOException {
		gfi.generateFile(validationDist, 3, 2);
	}

	private void generateTest(GenerateFilesInterface gfi) throws IOException {
		gfi.generateFile(testDist, 5, 2);

	}

	public static void main(String[] args) throws IOException {
		GenerateFilesInterface gfi = new GenerateMnistImgFiles();//new GenerateFaceImgFiles();
		LoadFiles gi = new LoadFiles();
		gi.generateInput(gfi);
		gi.generateTarget(gfi);
		gi.generateValidation(gfi);
		gi.generateTest(gfi);
	}
}
