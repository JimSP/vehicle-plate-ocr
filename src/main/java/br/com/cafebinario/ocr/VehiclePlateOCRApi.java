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
public class VehiclePlateOCRApi {

	@Autowired
	private RecognizeText recognizeText;
	
	@PostMapping("/{language}/ocr/{vehicleType}")
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.ON)
	public OcrResponse loadImage(
			@PathVariable(name = "language", required = false) final Language language,
			@PathVariable(name = "vehicleType", required = false) final VehicleType vehicleType,
			@RequestParam("file") final MultipartFile file) throws IOException {
		
		final Long begin = System.currentTimeMillis();
		
		final String identifier = UUID.randomUUID().toString();
		
		final List<String> result = recognizeText.extractTextFromImage(file.getInputStream(), identifier, language, vehicleType);
		
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
