package br.com.cafebinario.ocr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class OCRResultFilter {

	public List<String> toResult(final String[] text, final OcrType ocrType) {

		switch (ocrType.name()) {
		case "car":

			return car(text);

		case "motorcycle":

			return motorcycle(text);

		default:
			return other(text);
		}
	}

	private List<String> other(final String[] text) {

		return Arrays
					.asList(text)
					.stream()
					.map(mapper -> mapper.replaceAll("[^a-zA-Z0-9]", ""))
					.collect(Collectors.toList());

	}

	private List<String> car(final String[] text) {

		final List<String> result = Arrays.asList(text).stream()
				.filter(predicate -> predicate.matches("[A-Z]{3}[-][0-9]{4}")).collect(Collectors.toList());

		if (result.size() == 0) {
			throw new VehiclePlateNotIdentifier(result);
		}

		return result;

	}

	private List<String> motorcycle(final String[] text) {

		final List<String> result = Arrays.asList(text).stream()
				.filter(predicate -> predicate.length() == 4 || predicate.length() == 3).collect(Collectors.toList());
		;

		if (result.size() < 2) {
			throw new VehiclePlateNotIdentifier(result);
		}

		return result;
	}
}
