package com.github.agusmunioz.easypack.mock;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Utility class for mocking {@link FileUtils}.
 * 
 * @author agusmunioz
 *
 */
public class FileUtilsMock {

	/**
	 * Mocks the method {@link FileUtils#getFiles(File, String, String)}.
	 * 
	 * 
	 * @param folder
	 *            the folder where to start the search.
	 * @param includes
	 *            regular expression for including files.
	 * @param excludes
	 *            regular expression for excluding files.
	 * @param files
	 *            the list of files to return.
	 */
	public static void getFiles(File folder, String includes, String excludes,
			List<File> files) {

		try {
			PowerMockito.when(FileUtils.getFiles(folder, includes, excludes))
					.thenReturn(files);
		} catch (IOException e1) {
			// Won't happen, mock side effect.
		}

	}

	/**
	 * Verifies the method {@link FileUtils#copyFileToDirectory(File, File)} is
	 * not executed.
	 */
	public static void verifyNoFilesCopied() {

		PowerMockito.verifyStatic(Mockito.never());

		try {

			FileUtils.copyFileToDirectory(Mockito.any(File.class),
					Mockito.any(File.class));

		} catch (IOException e) {
			// Won't happen. Mock side effect.
		}

	}

	/**
	 * Verifies if the {@link FileUtils#copyFileToDirectory(File, File)} method
	 * is called.
	 * 
	 * @param file
	 *            the file to be copied.
	 * @param target
	 *            the destination folder.
	 */
	public static void verifyCopied(File file, File target) {

		PowerMockito.verifyStatic(Mockito.times(1));

		try {

			FileUtils.copyFileToDirectory(file, target);

		} catch (IOException e) {
			// Won't happen. Mock side effect.
		}
	}

}
