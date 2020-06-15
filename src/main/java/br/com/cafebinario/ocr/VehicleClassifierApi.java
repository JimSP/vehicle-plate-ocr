package br.com.cafebinario.ocr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class VehicleClassifierApi {

	@Autowired
	private RecognizeText recognizeText;
	
	@Autowired
	private OCRResultFilter ocrResultFilter;
	
	@Autowired
	private ImageObjectClassifier imageObjectClassifier;
	
	@PostMapping("classifier")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public ClassifierResult classifier(
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		
		final Long begin = System.currentTimeMillis();
		final String identifier = UUID.randomUUID().toString();
		final byte[] image = file.getBytes();
		
		final String[] text = recognizeText.extractTextFromImage(new ByteArrayInputStream(image), identifier, Language.english);
		final List<ImageRecognitionResult> imageRecognitionResult = imageObjectClassifier.classifier(new ByteArrayInputStream(image), identifier);
		
		final List<String> result = new ArrayList<>();

		try {
			final String resultCar = ocrResultFilter.toResult(text, VehicleType.car);
			result.add(resultCar);
		}catch (Exception e) {
		}
		
		try {
			final String resultMotorbike = ocrResultFilter.toResult(text, VehicleType.motorbike);
			result.add(resultMotorbike);
		}catch (Exception e) {
		}
		
		return ClassifierResult
						.builder()
						.imageRecognitionResult(imageRecognitionResult)
						.ocrResult(OcrResult
										.builder()
										.identifier(identifier)
										.inputFilename(file.getOriginalFilename())
										.result(result)
										.message("success")
										.processTime(System.currentTimeMillis() - begin)
										.build())
						.build();
	}
	
	@PostMapping("imageRecognition")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public List<ImageRecognitionResult> imageRecognition(
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		return imageObjectClassifier.classifier(file.getInputStream(), UUID.randomUUID().toString());
	}
	
	@PostMapping("ocr/{language}")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResult ocr(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final String result = ocrResultFilter.toResult(text, () -> "none");
		
		return OcrResult
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(Arrays.asList(result))
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
	
	@PostMapping("/ocr/car/{language}")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResult ocrCarPlate(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final String result = ocrResultFilter.toResult(text, VehicleType.car);
		
		return OcrResult
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(Arrays.asList(result))
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
	
	@PostMapping("ocr/motorbike/{language}")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResult loadPlateMotorcycle(
			@PathVariable(name = "language", required = false) final Language language,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final String[] text = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language);
		
		final String result = ocrResultFilter.toResult(text, VehicleType.motorbike);
		
		return OcrResult
				.builder()
				.identifier(identifier)
				.inputFilename(file.getOriginalFilename())
				.result(Arrays.asList(result))
				.message("success")
				.processTime(System.currentTimeMillis() - begin)
				.build();
	}
}
