package org.easypack.test.mock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.easypack.io.IoFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Mocks {@link FileWriter}.
 * 
 * @author agusmunioz
 *
 */
public class FileWriterMock {

	/**
	 * Returns a {@link StringWriter} so the start script content can be
	 * verified. Also it mocks {@link IoFactory} in order to use the
	 * {@link StringWriter}.
	 * 
	 * @return the string writer.
	 */
	public static StringWriter mock() {

		StringWriter writer = new StringWriter();

		try {

			PowerMockito.when(
					IoFactory.writer(Mockito.isA(File.class),
							Mockito.isA(String.class))).thenReturn(writer);
		} catch (IOException e) {
			// Won't happen just mock side effect.
		}

		return writer;
	}
}
