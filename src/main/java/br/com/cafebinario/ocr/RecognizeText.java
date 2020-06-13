package br.com.cafebinario.ocr;


import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import net.sourceforge.tess4j.ITesseract;

@Component
public class RecognizeText {
	
	private static final String INPUT_EXTENSION = "jpg";

	@Autowired
	@Qualifier("tesseractPortuguese")
	private ITesseract tesseractPortuguese;
	
	@Autowired
	@Qualifier("tesseractEnglish")
	private ITesseract tesseractEnglish;
	
	public List<String> extractTextFromImage(final InputStream inputStream, final String identifier, final Language language, final VehicleType vehicleType) {
		
		File file = createFile(inputStream, identifier);
		
		final File cvtFile = toCvt(file, identifier);

		try {
			switch (language) {
			case portuguese:
				
				return toResult(portuguese(cvtFile).split("[|\\n ]"), vehicleType);
				
			case english:
				
				return toResult(english(cvtFile).split("[|\\n ]"), vehicleType);

			default:
				
				return toResult(portuguese(cvtFile).split("[|\\n ]"), vehicleType);
			}
		}finally {
			file.deleteOnExit();
			cvtFile.deleteOnExit();
		}	
	}

	private List<String> toResult(final String[] text, final VehicleType vehicleType) {
		
		switch (vehicleType) {
		case car:
			
			return car(text);

		case motorcycle:	
		
			return motorcycle(text);
			
		default:
			return car(text);
		}
	}

	private File toCvt(File file, final String identifier) {
		final Mat origin = Imgcodecs.imread(file.getAbsolutePath());
		
		final Mat gray = new Mat();
		
		final String temp = String.format("images-temp/%s.png", identifier);
		
		Imgproc.cvtColor(origin, gray, COLOR_BGR2GRAY);
		Imgcodecs.imwrite(temp, gray);
		
		final File tmpFile = new File(temp);
		
		return tmpFile;
	}
	
	@SneakyThrows
	private File createFile(final InputStream inputStream, final String identifier) {
		
		final File file = File.createTempFile(identifier, INPUT_EXTENSION);
		
		FileUtils.copyInputStreamToFile(inputStream, file);
		
		return file;
	}

	@SneakyThrows
	private String english(final File imageFile) {
		
		return tesseractEnglish.doOCR(imageFile);
	}

	@SneakyThrows
	private String portuguese(final File imageFile) {
		
		return tesseractPortuguese.doOCR(imageFile);
	}
	
	private List<String> car(final String[] text) {
		
		final List<String> result = Arrays
				.asList(text)
				.stream()
				.filter(predicate->predicate.matches("[A-Z]{3}[-][0-9]{4}"))
				.collect(Collectors.toList());
		
		if(result.size() == 0) {
			throw new VehiclePlateNotIdentifier(result);
		}
		
		return result;
				
	}
	
	private List<String> motorcycle(final String[] text) {
		
		final List<String> result = Arrays
				.asList(text)
				.stream()
				.filter(predicate->predicate.length() == 4 || predicate.length() == 3)
				.collect(Collectors.toList());;
		
		if(result.size() < 2) {
			throw new VehiclePlateNotIdentifier(result);
		}
				
		return result;
	}
}
