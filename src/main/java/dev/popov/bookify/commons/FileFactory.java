package dev.popov.bookify.commons;

import java.io.File;
import java.io.IOException;

public class FileFactory {
	public File createTempFile(String fileName) throws IOException {
		return File.createTempFile("tmp", fileName);
	}
}
