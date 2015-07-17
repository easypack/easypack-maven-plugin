package com.github.easypack.test.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;

/**
 * Utility class for reading script examples from the classpath.
 * 
 * @author agusmunioz
 *
 */
public class ScriptTestReader {

	/**
	 * 
	 * Gets the script example content.
	 * 
	 * @param platform
	 *            the platform of the expected script.
	 * 
	 * @param postFix
	 *            the last part of the file name with the expected content (
	 *            /org/easypack/script/test-{platform}-'postFix').
	 * 
	 * @return the content.
	 * 
	 * @throws RuntimeException
	 *             if there is an error when getting the file content.
	 */
	public static String content(String name, String postFix) {

		try {

			String file = "/com/github/easypack/script/test-"
					+ name.toLowerCase();

			if (postFix != null && !postFix.isEmpty()) {
				file = file + "-" + postFix;
			}

			File script = new File(ScriptTestReader.class.getResource(file).toURI());

			return IOUtils.toString(new FileReader(script));

		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}

	}
}
