package com.github.easypack.script;

import org.junit.Assert;
import org.junit.Test;

import com.github.easypack.io.PathSeparator;
import com.github.easypack.platform.Platform;

/**
 * Unit test for {@link PreStart}.
 * 
 * @author agusmunioz
 * 
 */
public class PreStartTest {

	/**
	 * Test the default values are set.
	 */
	@Test
	public void defaultValues() {

		PreStart preStart = new PreStart();

		Assert.assertEquals("Invalid default value for Windows",
				PreStart.DEFAULT_WINDOWS, preStart.windows());

		Assert.assertEquals("Invalid default value for Linux",
				PreStart.DEFAULT_LINUX, preStart.linux());

	}

	/**
	 * Test the behavior with Linux platform.
	 */
	@Test
	public void behaveWithLinux() {

		PreStart preStart = new PreStart();

		String expected = "src" +  PathSeparator.get() + "main"
				+  PathSeparator.get() + "resources" +  PathSeparator.get()
				+ "bin" +  PathSeparator.get() + "linux";

		preStart.setLinux(expected);

		String path = Platform.LINUX.behave(preStart);

		Assert.assertEquals("Invalid behaviour for Linux", expected, path);

	}

	/**
	 * Test the behavior with Windows platform.
	 */
	@Test
	public void behaveWithWindows() {

		PreStart preStart = new PreStart();

		String expected = "src" +  PathSeparator.get() + "main"
				+  PathSeparator.get() + "resources" +  PathSeparator.get()
				+ "bin" +  PathSeparator.get() + "windows";

		preStart.setWindows(expected);

		String path = Platform.WINDOWS.behave(preStart);

		Assert.assertEquals("Invalid behaviour for Windows", expected, path);

	}
}
