package br.com.cafebinario.ocr;

import javax.annotation.PostConstruct;

import org.opencv.objdetect.CascadeClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import nu.pattern.OpenCV;

@Configuration
public class OpenCVConfiguration {
	
	@PostConstruct
	public void load() {
		OpenCV.loadShared();
	}

	@Bean("tesseractPortuguese")
	public ITesseract tesseractPortuguese() {
		final Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("D:\\tesseract\\Tesseract-OCR\\tessdata");
		tesseract.setLanguage("por");
		tesseract.setHocr(false);
		
		return tesseract;
	}
	
	@Bean("tesseractEnglish")
	public ITesseract tesseractEnglish() {
		final Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("D:\\tesseract\\Tesseract-OCR\\tessdata");
		tesseract.setLanguage("eng");
		tesseract.setHocr(false);
		
		return tesseract;
	}
	
	@Bean("haarcascadeCar")
	public CascadeClassifier haarcascadeCar() {
		return new CascadeClassifier("data/haarcascade_car.xml");
	}
}
