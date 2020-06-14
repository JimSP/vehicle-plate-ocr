package br.com.cafebinario.ocr;


import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import net.sourceforge.tess4j.ITesseract;

@Component
public class RecognizeText {

	@Autowired
	@Qualifier("tesseractPortuguese")
	private ITesseract tesseractPortuguese;
	
	@Autowired
	@Qualifier("tesseractEnglish")
	private ITesseract tesseractEnglish;
	
	public String[] extractTextFromImage(final InputStream inputStream, final String identifier, final Language language) {
		
		final File file = CreateFile.createFile(inputStream, identifier);
		
		final File cvtFile = ToCvtTransform.toCvtFile(file, identifier);

		try {
			switch (language) {
			case portuguese:
				
				return portuguese(cvtFile).split("[|\\n ]");
				
			case english:
				
				return english(cvtFile).split("[|\\n ]");

			default:
				
				return portuguese(cvtFile).split("[|\\n ]");
			}
		}finally {
			file.delete();
			cvtFile.delete();
		}	
	}


	@SneakyThrows
	private String english(final File imageFile) {
		
		return tesseractEnglish.doOCR(imageFile);
	}

	@SneakyThrows
	private String portuguese(final File imageFile) {
		
		return tesseractPortuguese.doOCR(imageFile);
	}
}
