package org.easypack.script;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ScriptSanitizer}
 * 
 * @author agusmunioz
 * 
 */
public class ScriptSanitizerTest {

	/**
	 * Test sanitizing empty parameters.
	 */
	@Test
	public void empty() {

		Assert.assertEquals("Incorrect sanitation of empty parameters", "",
				ScriptSanitizer.sanitize(""));
	}

	/**
	 * Test sanitizing parameters without new lines and tabs.
	 */
	@Test
	public void singleLine() {

		Assert.assertEquals("Incorrect sanitation of single line parameters",
				"-Xmx6G -Djava.io.tmp=/tmp",
				ScriptSanitizer.sanitize("-Xmx6G -Djava.io.tmp=/tmp"));
	}

	/**
	 * Test sanitizing parameters with new lines.
	 */
	@Test
	public void multipleLines() {

		String expected = "-Xmx6G -Djava.io.tmp=/tmp -Xms1G";

		Assert.assertEquals("Incorrect sanitation of multi-line parameters",
				expected, ScriptSanitizer
						.sanitize("-Xmx6G \n -Djava.io.tmp=/tmp \n -Xms1G"));

		Assert.assertEquals("Incorrect sanitation of multi-line parameters",
				expected, ScriptSanitizer
						.sanitize("-Xmx6G \r -Djava.io.tmp=/tmp \r -Xms1G"));
	}

	/**
	 * Test sanitizing parameters with tabs.
	 */
	@Test
	public void tabs() {

		Assert.assertEquals("Incorrect sanitation of parameters with tabs",
				"-Xmx6G -Djava.io.tmp=/tmp",
				ScriptSanitizer.sanitize("-Xmx6G \t\t-Djava.io.tmp=/tmp"));
	}

	/**
	 * Test sanitizing parameters with new lines and tabs.
	 */
	@Test
	public void newLinesAndTabs() {

		String expected = "-Xmx6G -Djava.io.tmp=/tmp";

		Assert.assertEquals(
				"Incorrect sanitation of parameters with new lines and tabs",
				expected,
				ScriptSanitizer.sanitize("-Xmx6G \n\t\t\t-Djava.io.tmp=/tmp"));

		Assert.assertEquals(
				"Incorrect sanitation of parameters with new lines and tabs",
				expected,
				ScriptSanitizer.sanitize("-Xmx6G \r\t\t\t-Djava.io.tmp=/tmp"));
	}

	/**
	 * Test the constructor cannot be called.
	 * 
	 * @throws IllegalAccessException
	 */
	@Test(expected = InvocationTargetException.class)
	public void constructor() throws Exception {

		Constructor<?> co = ScriptSanitizer.class.getDeclaredConstructors()[0];
		co.setAccessible(true);

		co.newInstance();
	}
}
