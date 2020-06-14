package br.com.cafebinario.ocr;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;

@RestController
public class OCRApi {

	@Autowired
	private RecognizeText recognizeText;
	
	@Autowired
	private OCRResultFilter ocrResultFilter;
	
	@Autowired
	private ImageObjectClassifier imageObjectClassifier;
	
	@PostMapping("/{language}/imageRecognition")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public List<String> imageRecognition(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		return imageObjectClassifier.classifier(file.getInputStream(), UUID.randomUUID().toString());
	}
	
	@PostMapping("/{language}/ocr")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResponse ocr(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final List<String> result = ocrResultFilter.toResult(text, () -> "none");
		
		return OcrResponse
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(result)
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
	
	@PostMapping("/{language}/ocr/car")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResponse ocrCarPlate(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final List<String> result = ocrResultFilter.toResult(text, VehicleType.car);
		
		return OcrResponse
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(result)
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
	
	@PostMapping("/{language}/ocr/motorcycle")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResponse loadPlateMotorcycle(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final List<String> result = ocrResultFilter.toResult(text, VehicleType.motorcycle);
		
		return OcrResponse
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(result)
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
}
