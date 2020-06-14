package br.com.cafebinario.ocr;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ToCvtTransform {

	public static File toCvtFile(final File file, final String identifier) {
		final Mat origin = Imgcodecs.imread(file.getAbsolutePath());
		
		final Mat gray = new Mat();
		
		final String temp = String.format("images-temp/%s.png", identifier);
		
		Imgproc.cvtColor(origin, gray, COLOR_BGR2GRAY);
		Imgcodecs.imwrite(temp, gray);
		
		final File tmpFile = new File(temp);
		
		return tmpFile;
	}
	
	public static Mat toCvtMat(final File file, final String identifier) {
		final Mat origin = Imgcodecs.imread(file.getAbsolutePath());
		
		final Mat gray = new Mat();
		
		final String temp = String.format("images-temp/%s.png", identifier);
		
		Imgproc.cvtColor(origin, gray, COLOR_BGR2GRAY);
		Imgcodecs.imwrite(temp, gray);
		
		return gray;
	}
}
