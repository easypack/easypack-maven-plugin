package com.github.easypack.script;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.easypack.io.FileContent;
import com.github.easypack.mock.FileMock;
import com.github.easypack.script.BashUtils;

/**
 * Unit test for {@link BashUtils}.
 * 
 * @author agusmunioz
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FileContent.class)
public class BashUtilsTest {

	/**
	 * Test the bash script determination with a bash script.
	 */
	@Test
	public void isBash() {

		try {

			String script = "#!/bin/bash \n echo 'hola'";

			File file = FileMock.mock();

			PowerMockito.mockStatic(FileContent.class);

			PowerMockito.when(FileContent.get(file.getAbsolutePath()))
					.thenReturn(script);

			boolean is = BashUtils.isBash(file);

			Assert.assertTrue("A bash script is not considered as such.", is);

		} catch (IOException e) {
			Assert.fail("Unexepcted exception. " + e);
		}

	}

	/**
	 * Test the bash script determination with a content that is not bash.
	 */
	@Test
	public void isNotBash() {

		try {

			String script = "A not bash script";

			File file = FileMock.mock();

			PowerMockito.mockStatic(FileContent.class);

			PowerMockito.when(FileContent.get(file.getAbsolutePath()))
					.thenReturn(script);

			boolean is = BashUtils.isBash(file);

			Assert.assertFalse("A not bash script is considered as such.", is);

		} catch (IOException e) {
			Assert.fail("Unexepcted exception. " + e);
		}

	}

	/**
	 * Test the case when there is an I/O exception when reading the script
	 * file.
	 * 
	 * @throws IOException
	 *             won't happen, mocking side effect.
	 */
	@Test
	public void ioException() {

		try {

			File file = FileMock.mock();

			PowerMockito.mockStatic(FileContent.class);

			PowerMockito.when(FileContent.get(file.getAbsolutePath()))
					.thenThrow(new IOException());

			boolean is = BashUtils.isBash(file);

			Assert.assertFalse(
					"Unexpected bash determination on a I/O exception.", is);

		} catch (IOException e) {
			Assert.fail("Unexepcted exception. " + e);
		}
	}
}
