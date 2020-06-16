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

		return extractTextFromImage(cvtFile.getAbsolutePath(), identifier, language);
	}
	
	public String[] extractTextFromImage(final String cvtFile, final String identifier, final Language language) {

		final File file = new File(cvtFile);
		
		try {
			switch (language) {
			case portuguese:
				
				return portuguese(file).split("[|\\n ]");
				
			case english:
				
				return english(file).split("[|\\n ]");

			default:
				
				return portuguese(file).split("[|\\n ]");
			}
		}finally {
			//file.delete();
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
