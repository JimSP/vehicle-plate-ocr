package br.com.cafebinario.ocr;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ClassifierResult implements Serializable{

	private static final long serialVersionUID = 5717609988380328871L;

	private final List<ImageRecognitionResult> imageRecognitionResult;
	
	private final OcrResult ocrResult;
}
