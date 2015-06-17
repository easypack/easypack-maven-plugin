package com.github.easypack.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Utility class for files and related class creation.
 * 
 * @author agusmunioz
 * 
 */
public class IoFactory {

	private IoFactory() {
		throw new IllegalAccessError("Constuctor cannot be called,"
				+ " not even with reflection.");
	}

	/**
	 * Creates a file.
	 * 
	 * @param path
	 *            the file path including its name.
	 * 
	 * @return a not null file.
	 */
	public static File file(String path) {

		return new File(path);
	}

	/**
	 * Creates a list of files.
	 * 
	 * @param directory
	 *            the directory path that contains the files.
	 * 
	 * @param paths
	 *            a list of relative paths to the directory, for each files.
	 * 
	 * @return the list of files.
	 */
	public static File[] files(String directory, String[] paths) {

		File[] files = new File[paths.length];

		String root = directory;

		if (!root.endsWith(File.separator)) {
			root += File.separator;
		}

		for (int i = 0; i < paths.length; i++) {
			files[i] = file(root + paths[i]);
		}

		return files;
	}

	/**
	 * Creates a IO writer.
	 * 
	 * @param folder
	 *            the folder that will contain the file to be written.
	 * 
	 * @param name
	 *            the name of the file to be written.
	 * 
	 * @return the writer.
	 * 
	 * @throws IOException
	 *             if there is an error when creating the writer.
	 */
	public static Writer writer(File folder, String name) throws IOException {

		return new FileWriter(new File(folder, name));
	}

	/**
	 * Creates a file reader.
	 * 
	 * @param file
	 *            the file to be read.
	 * @return the reader.
	 * @throws FileNotFoundException
	 *             if the file doesn't exist.
	 */
	public static FileReader reader(File file) throws FileNotFoundException {
		return new FileReader(file);
	}

}
