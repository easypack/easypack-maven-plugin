package com.github.easypack.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link IoFactory}.
 * 
 * @author agusmunioz
 *
 */
public class IoFactoryTest {

	/**
	 * Test the creation of a File.
	 */
	@Test
	public void file() {

		String path = "a-file";

		File file = IoFactory.file(path);

		Assert.assertNotNull("Null file returned", file);

		Assert.assertEquals("Unexpected file path.", path, file.getPath());
	}

	/**
	 * Test the creation of a {@link FileWriter}.
	 */
	@Test
	public void writter() {

		try {

			File folder = new File(System.getProperty("java.io.tmpdir"));

			Writer writer = IoFactory.writer(folder, "start.sh");

			Assert.assertNotNull("Unexpected null writer.", writer);

		} catch (IOException e) {

			e.printStackTrace();

			Assert.fail("Unexpected exception." + e.getMessage());
		}
	}

	/**
	 * Test the creation of an array of files.
	 */
	@Test
	public void files() {

		String folder = System.getProperty("java.io.tmpdir");

		if (!folder.endsWith( PathSeparator.get())) {
			folder +=  PathSeparator.get();
		}

		String[] paths = new String[] { "hello", "bye" };

		File[] files = IoFactory.files(folder, paths);

		Assert.assertEquals("Unexpected amount of created files", paths.length,
				files.length);

		for (File file : files) {

			boolean created = false;

			for (String path : paths) {
				created = created || (file.getPath().equals(folder + path));
			}

			Assert.assertTrue("Unexpected file created", created);
		}

	}

	/**
	 * Test the creation of a {@link FileReader} with a valid file.
	 */
	@Test
	public void reader() {

		try {

			File f = new File(this.getClass()
					.getResource("/com/github/easypack/io/test").toURI());

			FileReader reader = IoFactory.reader(f);

			Assert.assertNotNull("Null reader returned.", reader);

		} catch (FileNotFoundException | URISyntaxException e) {

			Assert.fail("Unexpected exception: " + e.getClass());
		}

	}

	/**
	 * Test the creation of a {@link FileReader} with a not existing file.
	 * 
	 * @throws FileNotFoundException
	 *             expected
	 */
	@Test(expected = FileNotFoundException.class)
	public void readerFail() throws FileNotFoundException {

		IoFactory.reader(new File("not-existing"));

	}

	/**
	 * Test the constructor cannot be called.
	 * 
	 * @throws IllegalAccessException
	 */
	@Test(expected = InvocationTargetException.class)
	public void constructor() throws Exception {

		Constructor<?> co = IoFactory.class.getDeclaredConstructors()[0];
		co.setAccessible(true);
		co.newInstance();
	}
}
