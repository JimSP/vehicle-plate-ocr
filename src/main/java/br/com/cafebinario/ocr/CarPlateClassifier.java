package br.com.cafebinario.ocr;

import java.io.File;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarPlateClassifier {

	private static final int GRAY = 0;
	private static final int COUNTORNED = 1;
	
	private static final String cvtRectanguleFilename = "images-temp/%s_%s.png";
	
	@Autowired
	private ROIExtractor roiExtractor;
	
	@Autowired
	private RecognizeText recognizeText;

	public String[] recognizer(final InputStream inputStream, final String identifier) {
		
		final File file = CreateFile.createFile(inputStream, identifier);
		final Mat raw = Imgcodecs.imread(file.getAbsolutePath());
		final Mat[] roi = roiExtractor.getROI(raw);
		
		final String fileNameGray = String.format(cvtRectanguleFilename, GRAY, identifier);
		final String fileNameCountorned = String.format(cvtRectanguleFilename, COUNTORNED, identifier);
		
		Imgcodecs.imwrite(fileNameGray, roi[GRAY]);
		Imgcodecs.imwrite(fileNameCountorned, roi[COUNTORNED]);

		return recognizeText.extractTextFromImage(fileNameGray, identifier, Language.english);
	}
}
