package br.com.cafebinario.ocr;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VehiclePlateOcrAdvice {

	@ExceptionHandler(VehiclePlateNotIdentifier.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public OcrResponse vehiclePlateNotIdentifier(final VehiclePlateNotIdentifier vehiclePlateNotIdentifier){
		
		return OcrResponse
				.builder()
				.result(vehiclePlateNotIdentifier.getResult())
				.message("identification was not possible")
				.build();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public OcrResponse exception(final Exception exception){
		
		return OcrResponse
				.builder()
				.message("error during processing")
				.build();
	}
}
