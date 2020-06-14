package br.com.cafebinario.ocr;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class ImageRecognitionResult implements Serializable{
	
	private static final long serialVersionUID = 6440856878941301284L;

	private final String label;
	private final double accuracy;
	private final double x;
	private final double y;
	private final double width;
	private final double height;
}
