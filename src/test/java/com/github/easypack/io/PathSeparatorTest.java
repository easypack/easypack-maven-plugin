package com.github.easypack.io;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PathSeparator}.
 * 
 * @author agusmunioz
 * 
 */
public class PathSeparatorTest {

	/**
	 * Test the obtain of the path separator character.
	 */
	@Test
	public void get() {

		String expected = File.separator;

		String actual = PathSeparator.get();

		Assert.assertEquals("Unexpected path separator character.", expected,
				actual);
	}

	/**
	 * Test the migration of a path character.
	 */
	@Test
	public void migrate() {

		Assert.assertEquals(
				"Unexpected migration of Windows separator character.", "\\",
				PathSeparator.migrate("/"));

		Assert.assertEquals(
				"Unexpected migration of Linux separator character.", "/",
				PathSeparator.migrate("\\"));
	}
}
