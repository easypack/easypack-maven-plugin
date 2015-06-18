package com.github.easypack.io;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for building file system paths.
 * 
 * @author agusmunioz
 * 
 */
public final class PathUtils {

	private PathUtils() {
		throw new IllegalAccessError("Constuctor cannot be called, "
				+ "not even with reflection.");
	}

	/**
	 * Builds a path String using the specified names.
	 * 
	 * @param names
	 *            folder names, or in the case of the last name, a file name.
	 * 
	 * @return all names joined with '/' (not at the beginning nor at the end)
	 *         or an empty String if no name is passed.
	 */
	public static String path(String... names) {
		return StringUtils.join(names, PathSeparator.get());
	}

	/**
	 * Build a file full/absolute name.
	 * 
	 * @param extension
	 *            the file extension.
	 * @param names
	 *            the folders names and the file name at the last String.
	 * @return the file full name.
	 */
	public static String file(String extension, String... names) {

		return PathUtils.path(names) + "." + extension;
	}

	/**
	 * Subtracts part of a path. It doesn't follow any arithmetic property of
	 * the subtraction operation.
	 * 
	 * <p>
	 * PathUtils.subtract("/a/b/c", "/a/b") = "/c"
	 * </p>
	 * 
	 * <p>
	 * PathUtils.subtract("/a/b/c/d", "/b/c") = "/a/c"
	 * </p>
	 * 
	 * @param minuend
	 *            the original path.
	 * 
	 * @param subtrahend
	 *            the path to be subtracted.
	 * 
	 * @return the remaining path.
	 */
	public static String subtract(String minuend, String subtrahend) {

		return minuend.replace(subtrahend, "");
	}

	/**
	 * Transform a path into a valid one in the hosting Operating System,
	 * specifically by changing the separator character to the expected.
	 * 
	 * @param path
	 *            the path to be "osifyed".
	 * 
	 * @return the path with valid separator.
	 */
	public static String osify(String path) {

		if (path.contains(PathSeparator.get())) {
			return path;
		}

		return path.replace(PathSeparator.migrate(PathSeparator.get()),
				PathSeparator.get());
	}
}