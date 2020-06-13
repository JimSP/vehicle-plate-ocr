package br.com.cafebinario.ocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.cafebinario.logger.EnableLog;

@SpringBootApplication
@EnableLog
public class OcrApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(OcrApplication.class, args);
	}

}
