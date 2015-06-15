package org.easypack.script;

import java.io.File;
import java.io.IOException;

import org.easypack.io.FileContent;

/**
 * Utility class for determining if a file is a bash script.
 * 
 * @author agusmunioz
 *
 */
public class BashUtils {

	private static final String BASH_HEADER = "#!/bin/bash";

	/**
	 * Determines if the file is a bash script based on the script header.
	 * 
	 * @param file
	 *            the file containing the script content.
	 *            
	 * @return true if it is a bash script, false otherwise.
	 */
	public static boolean isBash(File file) {

		try {

			return FileContent.get(file.getAbsolutePath()).startsWith(
					BASH_HEADER);

		} catch (IOException e) {
			return false;
		}
	}
}
