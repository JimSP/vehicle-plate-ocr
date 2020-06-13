package br.com.cafebinario.ocr;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class OcrResponse implements Serializable {

	private static final long serialVersionUID = -2050522016190487676L;

	private final List<String> result;
	private final String identifier;
	private final String inputFilename;
	private final Long processTime;
	private final String message;

}
