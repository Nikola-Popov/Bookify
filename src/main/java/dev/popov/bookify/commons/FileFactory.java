package dev.popov.bookify.commons;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class FileFactory {
	public File createTempFile(String fileName) throws IOException {
		return File.createTempFile("tmp", fileName);
	}
}
