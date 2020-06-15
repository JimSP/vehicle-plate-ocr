package br.com.cafebinario.ocr;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OCRResultFilter {

	public String toResult(final String[] text, final OcrType ocrType) {

		switch (ocrType.name()) {
		case "car":

			return car(text);

		case "motorbike":

			return motorcycle(text);

		default:
			return other(text);
		}
	}

	private String other(final String[] text) {

		return Arrays
					.asList(text)
					.stream()
					.map(mapper -> mapper.replaceAll("[^a-zA-Z0-9\\-]", ""))
					.filter(predicate->!"".equals(predicate))
					.reduce((a,b)->a.concat(b))
					.orElseThrow(()->new VehiclePlateNotIdentifier(text));

	}

	private String car(final String[] text) {

		return Arrays
				.asList(text)
				.stream()
				.map(mapper -> mapper.replaceAll("[^A-Z0-9\\-]", ""))
				.filter(predicate -> predicate.length() == 7 || predicate.length() == 8)
				.filter(predicate->!"".equals(predicate))
				.reduce((a,b)->a.concat(b))
				.orElseThrow(()->new VehiclePlateNotIdentifier(text));

	}

	private String motorcycle(final String[] text) {

		return Arrays
				.asList(text)
				.stream()
				.map(mapper -> mapper.replaceAll("[^A-Z0-9\\-]", ""))
				.filter(predicate -> predicate.length() == 4 || predicate.length() == 3)
				.filter(predicate->!"".equals(predicate))
				.reduce((a,b)->a.concat(b))
				.orElseThrow(()->new VehiclePlateNotIdentifier(text));
	}
}
