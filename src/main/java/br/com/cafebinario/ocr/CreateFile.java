package br.com.cafebinario.ocr;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;

public class CreateFile {
	
	@SneakyThrows
	public static File createFile(final InputStream inputStream, final String identifier) {
		
		final File file = File.createTempFile(identifier, "");
		
		FileUtils.copyInputStreamToFile(inputStream, file);
		
		return file;
	}
}
