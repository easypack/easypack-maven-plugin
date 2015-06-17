package com.github.agusmunioz.easypack.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.agusmunioz.easypack.io.FileContent;
import com.github.agusmunioz.easypack.io.IoFactory;
import com.github.agusmunioz.easypack.mock.FileMock;

/**
 * Unit test for {@link FileContent}.
 * 
 * @author agusmunioz
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IoFactory.class, IOUtils.class })
public class FileContentTest {

	@Before
	public void mockStatics() {
		PowerMockito.mockStatic(IoFactory.class);
		PowerMockito.mockStatic(IOUtils.class);
	}

	/**
	 * Test the content extraction with an existing file.
	 */
	@Test
	public void existingFile() {

		try {

			String expected = "This is the file content";

			File file = FileMock.mock(true);

			PowerMockito.when(IoFactory.file(file.getPath())).thenReturn(file);

			FileReader reader = PowerMockito.mock(FileReader.class);

			PowerMockito.when(IoFactory.reader(file)).thenReturn(reader);

			PowerMockito.when(IOUtils.toString(reader)).thenReturn(expected);

			String content = FileContent.get(file.getPath());

			Assert.assertEquals("Unexpected file contet.", expected, content);

		} catch (IOException e) {
			Assert.fail("Unexpected exception. " + e.getMessage());
		}

	}

	/**
	 * Test the content extraction with an not existing file
	 */
	@Test
	public void notExistingFile() {

		try {

			File file =FileMock.mock(false);

			PowerMockito.when(IoFactory.file(file.getPath())).thenReturn(file);

			String content = FileContent.get(file.getPath());

			Assert.assertNull("Content returned with an not existing file.",
					content);

		} catch (IOException e) {
			Assert.fail("Unexpected exception. " + e.getMessage());
		}
	}

	/**
	 * Test an IO exception is thrown when such exception takes places during
	 * content extraction.
	 * 
	 * @throws IOException
	 *             expected exception.
	 */
	@Test(expected = IOException.class)
	public void ioException() throws IOException {

		try {

			File file = FileMock.mock(true);

			PowerMockito.when(IoFactory.file(file.getPath())).thenReturn(file);

			FileReader reader = PowerMockito.mock(FileReader.class);

			PowerMockito.when(IoFactory.reader(file)).thenReturn(reader);

			PowerMockito.when(IOUtils.toString(reader)).thenThrow(
					new IOException());

			FileContent.get(file.getPath());

		} catch (FileNotFoundException e) {

			Assert.fail("Unexpected FileNotFoundException.");
		}
	}

	
}
