package com.github.easypack.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.github.easypack.io.PathUtils;

/**
 * Unit test for {@link PathUtils}.
 * 
 * @author agusmunioz
 * 
 */
public class PathUtilsTest {

	/**
	 * Test the path built with just one name.
	 */
	@Test
	public void oneName() {

		String path = PathUtils.path("myfolder");

		Assert.assertEquals("Incorrect path for one name.", "myfolder", path);
	}

	/**
	 * Test the path built with several names.
	 */
	@Test
	public void severalNames() {

		String expected = "myfolder" + PathUtils.SEPARATOR + "another"
				+ PathUtils.SEPARATOR + "athird";

		String path = PathUtils.path("myfolder", "another", "athird");

		Assert.assertEquals("Incorrect path for several name.", expected, path);
	}

	/**
	 * Test the path build with no names.
	 */
	@Test
	public void noNames() {

		String path = PathUtils.path();

		Assert.assertTrue("Incorrect path for no name.", path.isEmpty());
	}

	@Test
	public void fileName() {

		String expected = "$HOME" + PathUtils.SEPARATOR + "varnish"
				+ PathUtils.SEPARATOR + "default.vcl";

		String file = PathUtils.file("vcl", "$HOME", "varnish", "default");

		Assert.assertEquals("Incorrect file full name.", expected, file);
	}

	/**
	 * Test the path subtraction of the first part of a path
	 */
	@Test
	public void subtractFirstPart() {

		String minuend = "src" + PathUtils.SEPARATOR + "main"
				+ PathUtils.SEPARATOR + "resources" + PathUtils.SEPARATOR
				+ "bin" + PathUtils.SEPARATOR + "start-linux";

		String subtrahend = "src" + PathUtils.SEPARATOR + "main"
				+ PathUtils.SEPARATOR + "resources" + PathUtils.SEPARATOR;

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "bin" + PathUtils.SEPARATOR + "start-linux";

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Test the path subtraction of the last part of a path
	 */
	@Test
	public void subtractLastPart() {

		String minuend = "src" + PathUtils.SEPARATOR + "main"
				+ PathUtils.SEPARATOR + "resources" + PathUtils.SEPARATOR
				+ "bin" + PathUtils.SEPARATOR + "start-linux";

		String subtrahend = "resources" + PathUtils.SEPARATOR + "bin"
				+ PathUtils.SEPARATOR + "start-linux";

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "src" + PathUtils.SEPARATOR + "main"
				+ PathUtils.SEPARATOR;

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Test the path subtraction of the middle part of a path
	 */
	@Test
	public void subtractMiddlePart() {

		String minuend = "src" + PathUtils.SEPARATOR + "main"
				+ PathUtils.SEPARATOR + "resources" + PathUtils.SEPARATOR
				+ "bin" + PathUtils.SEPARATOR + "start-linux";

		String subtrahend = PathUtils.SEPARATOR + "main" + PathUtils.SEPARATOR
				+ "resources";

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "src" + PathUtils.SEPARATOR + "bin"
				+ PathUtils.SEPARATOR + "start-linux";

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Transform a windows path to a Linux.
	 */
	@Test
	@Ignore
	public void osifyWinToLinux() {

		String osifyed = PathUtils.osify("\\src\\main\\resources");

		String expected = "/src/main/resources";

		Assert.assertEquals("Unexpected path from Win to Linux.", expected,
				osifyed);
	}

	/**
	 * Transform a Linux path to a Linux.
	 */
	@Test
	@Ignore
	public void osifyLinuxToLinux() {

		String osifyed = PathUtils.osify("/src/main/resources");

		String expected = "/src/main/resources";

		Assert.assertEquals("Unexpected path from Linux to Linux.", expected,
				osifyed);
	}

	/**
	 * Test the constructor cannot be called.
	 * 
	 * @throws IllegalAccessException
	 */
	@Test(expected = InvocationTargetException.class)
	public void constructor() throws Exception {

		Constructor<?> co = PathUtils.class.getDeclaredConstructors()[0];
		co.setAccessible(true);

		co.newInstance();
	}
}
