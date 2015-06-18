package com.github.easypack.io;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for {@link PathUtils}.
 * 
 * @author agusmunioz
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PathSeparator.class)
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

		String expected = "myfolder" + PathSeparator.get() + "another"
				+ PathSeparator.get() + "athird";

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

		String expected = "$HOME" + PathSeparator.get() + "varnish"
				+ PathSeparator.get() + "default.vcl";

		String file = PathUtils.file("vcl", "$HOME", "varnish", "default");

		Assert.assertEquals("Incorrect file full name.", expected, file);
	}

	/**
	 * Test the path subtraction of the first part of a path
	 */
	@Test
	public void subtractFirstPart() {

		String minuend = "src" + PathSeparator.get() + "main"
				+ PathSeparator.get() + "resources" + PathSeparator.get()
				+ "bin" + PathSeparator.get() + "start-linux";

		String subtrahend = "src" + PathSeparator.get() + "main"
				+ PathSeparator.get() + "resources" + PathSeparator.get();

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "bin" + PathSeparator.get() + "start-linux";

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Test the path subtraction of the last part of a path
	 */
	@Test
	public void subtractLastPart() {

		String minuend = "src" + PathSeparator.get() + "main"
				+ PathSeparator.get() + "resources" + PathSeparator.get()
				+ "bin" + PathSeparator.get() + "start-linux";

		String subtrahend = "resources" + PathSeparator.get() + "bin"
				+ PathSeparator.get() + "start-linux";

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "src" + PathSeparator.get() + "main"
				+ PathSeparator.get();

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Test the path subtraction of the middle part of a path
	 */
	@Test
	public void subtractMiddlePart() {

		String minuend = "src" + PathSeparator.get() + "main"
				+ PathSeparator.get() + "resources" + PathSeparator.get()
				+ "bin" + PathSeparator.get() + "start-linux";

		String subtrahend = PathSeparator.get() + "main" + PathSeparator.get()
				+ "resources";

		String result = PathUtils.subtract(minuend, subtrahend);

		String expected = "src" + PathSeparator.get() + "bin"
				+ PathSeparator.get() + "start-linux";

		Assert.assertEquals("Unexpected path subtraction", expected, result);
	}

	/**
	 * Transform a windows path to a Linux.
	 */
	@Test
	public void osifyWinToLinux() {

		PowerMockito.mockStatic(PathSeparator.class);

		PowerMockito.when(PathSeparator.get()).thenReturn("/");

		PowerMockito.when(PathSeparator.migrate("/")).thenReturn("\\");

		String osifyed = PathUtils.osify("\\src\\main\\resources");

		String expected = "/src/main/resources";

		Assert.assertEquals("Unexpected path from Win to Linux.", expected,
				osifyed);
	}

	/**
	 * Transform a Linux path to a Linux.
	 */
	@Test
	public void osifyLinuxToLinux() {

		PowerMockito.mockStatic(PathSeparator.class);

		PowerMockito.when(PathSeparator.get()).thenReturn("/");

		String osifyed = PathUtils.osify("/src/main/resources");

		String expected = "/src/main/resources";

		Assert.assertEquals("Unexpected path from Linux to Linux.", expected,
				osifyed);
	}

	/**
	 * Transform a Linux path to a Windows.
	 */
	@Test
	public void osifyLinuxToWin() {

		PowerMockito.mockStatic(PathSeparator.class);

		PowerMockito.when(PathSeparator.get()).thenReturn("\\");

		PowerMockito.when(PathSeparator.migrate("\\")).thenReturn("/");

		String osifyed = PathUtils.osify("/src/main/resources");

		String expected = "\\src\\main\\resources";

		Assert.assertEquals("Unexpected path from Linux to Windows.", expected,
				osifyed);
	}

	/**
	 * Transform a Linux path to a Linux.
	 */
	@Test
	public void osifyWinToWin() {

		PowerMockito.mockStatic(PathSeparator.class);

		PowerMockito.when(PathSeparator.get()).thenReturn("\\");

		String osifyed = PathUtils.osify("\\src\\main\\resources");

		String expected = "\\src\\main\\resources";

		Assert.assertEquals("Unexpected path from Linux to Linux.", expected,
				osifyed);
	}
}
