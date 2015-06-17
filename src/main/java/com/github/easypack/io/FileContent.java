package com.github.easypack.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Utility class for getting a file content.
 * 
 * @author agusmunioz
 *
 */
public class FileContent {

	
	/**
	 * Gets a file content.
	 * 
	 * @param path
	 *            the file path.
	 * 
	 * @return the content as String or null if the file doesn't exists.
	 * 
	 * @throws IOException
	 *             if there is an error reading the file.
	 */
	public static String get(String path) throws IOException {

		File file = IoFactory.file(path);

		if (file.exists()) {
			return IOUtils.toString(IoFactory.reader(file));
		}

		return null;
	}

	
}
