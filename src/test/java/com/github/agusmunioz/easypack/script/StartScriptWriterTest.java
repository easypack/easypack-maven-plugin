package com.github.agusmunioz.easypack.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.agusmunioz.easypack.io.FileContent;
import com.github.agusmunioz.easypack.io.IoFactory;
import com.github.agusmunioz.easypack.mock.FileWriterMock;
import com.github.agusmunioz.easypack.platform.Platform;
import com.github.agusmunioz.easypack.script.PreStart;
import com.github.agusmunioz.easypack.script.StartScriptWriter;

/**
 * Unit test for {@link StartScriptWriter}.
 * 
 * @author agusmunioz
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IoFactory.class, FileContent.class })
public class StartScriptWriterTest {

	private static final String JAVA_OPTS = "-Xms1G -Xmx=3G -Denvironment=prod";

	private static final String JAVA_ARGS = "hello";

	private StartScriptWriter scriptWriter;

	private PreStart preStart;

	private File folder = new File(System.getProperty("java.io.tmpdir"));

	@Before
	public void init() {

		this.scriptWriter = new StartScriptWriter();

		this.preStart = new PreStart();

		this.scriptWriter.args("").folder(folder).jar("my-project").opts("")
				.preStart(preStart);

		PowerMockito.mockStatic(IoFactory.class);

		PowerMockito.mockStatic(FileContent.class);
	}

	/**
	 * Test the start script generation for all platforms with all default
	 * values.
	 * 
	 */
	@Test
	public void defaultValues() {

		this.assertScript("default", null);

	}

	/**
	 * Test the start script generation for Linux platform with all default
	 * values but options.
	 */
	@Test
	public void options() {

		this.scriptWriter.opts(JAVA_OPTS);

		this.assertScript("options", null);
	}

	/**
	 * Test the start script generation for all platforms with all default
	 * values but args.
	 */
	@Test
	public void args() {

		this.scriptWriter.args(JAVA_ARGS);

		this.assertScript("args", null);
	}

	/**
	 * Test the start script generation for all platforms with all default
	 * values but with each echo option.
	 */
	@Test
	public void echos() {

		for (String echo : new String[] { "all", "java" }) {

			this.scriptWriter.echo(echo);
			this.assertScript("echo-" + echo, null);
		}

	}

	/**
	 * Test the start script generation for all platforms with all default
	 * values but with a script to be executed before the java line.
	 */
	@Test
	public void preStart() {

		this.assertScript("pre-start", "echo 'Hola'");
	}

	/**
	 * Test the start script generation for all platforms with all values
	 * configured.
	 */
	@Test
	public void complete() {

		this.scriptWriter.args(JAVA_ARGS).folder(folder).jar("my-project")
				.opts(JAVA_OPTS).echo("all");

		this.assertScript("all-configured", "echo 'Hola'");
	}

	/**
	 * Compares the generated start script with the expected one for all
	 * platforms.
	 * 
	 * @param postFix
	 *            the last part of the file name with the expected content (
	 *            /org/easypack/script/test-{platform}-'postFix').
	 * 
	 * @param preStartContent
	 *            the content of the pre-start script (null if no content is
	 *            desired)
	 */
	private void assertScript(String postFix, String preStartContent) {

		for (Platform platform : Platform.values()) {

			String expected = this.content(platform, postFix);

			Writer writer = FileWriterMock.mock();

			try {

				Mockito.when(FileContent.get(platform.behave(this.preStart)))
						.thenReturn(preStartContent);

			} catch (IOException e) {
				// Won't happen is a mock side effect.

			}

			platform.behave(this.scriptWriter);

			Assert.assertEquals("Unexpected start script content.", expected,
					writer.toString());
		}

	}

	/**
	 * 
	 * Gets the script example content.
	 * 
	 * @param platform
	 *            the platform of the expected script.
	 * 
	 * @param postFix
	 *            the last part of the file name with the expected content (
	 *            /org/easypack/script/test-{platform}-'postFix').
	 * 
	 * @return the content.
	 * 
	 * @throws RuntimeException
	 *             if there is an error when getting the file content.
	 */
	private String content(Platform platform, String postFix) {

		try {

			File script = new File(this
					.getClass()
					.getResource(
							"/com/github/agusmunioz/easypack/script/test-"
									+ platform.name().toLowerCase() + "-"
									+ postFix).toURI());

			return IOUtils.toString(new FileReader(script));

		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}

	}
}
