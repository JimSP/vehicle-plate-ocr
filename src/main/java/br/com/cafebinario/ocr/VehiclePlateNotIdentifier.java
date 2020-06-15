package br.com.cafebinario.ocr;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehiclePlateNotIdentifier extends RuntimeException{


	private static final long serialVersionUID = 2600756302690429931L;
	
	private final String[] text;

}
