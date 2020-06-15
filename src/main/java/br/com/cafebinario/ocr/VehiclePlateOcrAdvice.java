package br.com.cafebinario.ocr;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VehiclePlateOcrAdvice {

	@ExceptionHandler(VehiclePlateNotIdentifier.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public OcrResult vehiclePlateNotIdentifier(final VehiclePlateNotIdentifier vehiclePlateNotIdentifier){
		
		return OcrResult
				.builder()
				.result(Arrays.asList(vehiclePlateNotIdentifier.getText()))
				.message("identification was not possible")
				.build();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public OcrResult exception(final Exception exception){
		
		return OcrResult
				.builder()
				.message("error during processing")
				.build();
	}
}
