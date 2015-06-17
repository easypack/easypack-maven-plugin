package com.github.agusmunioz.easypack.mock;

import java.io.File;

import org.powermock.api.mockito.PowerMockito;

/**
 * Utility class for mocking files.
 * 
 * @author agusmunioz
 *
 */
public class FileMock {

	/**
	 * Creates a mocked file.
	 * 
	 * @param exist
	 *            if the file exists.
	 * 
	 * @return the mocked file.
	 */
	public static File mock(boolean exist) {

		File file = PowerMockito.mock(File.class);

		PowerMockito.when(file.getPath()).thenReturn("/a-file");

		PowerMockito.when(file.getAbsolutePath()).thenReturn(
				System.getProperty("java.io.tmpdir") + "/a-file");

		PowerMockito.when(file.exists()).thenReturn(exist);

		return file;
	}

	/**
	 * Creates a mock file.
	 * 
	 * @return the mock.
	 */
	public static File mock() {

		return mock(true);
	}

}
