package com.github.easypack.script;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.easypack.io.FileContent;
import com.github.easypack.io.IoFactory;
import com.github.easypack.mock.FileWriterMock;
import com.github.easypack.platform.Platform;
import com.github.easypack.test.utils.ScriptTestReader;

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
				.process("my-project").preStart(preStart);

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
	 * Test when there is an IOexception when creating the writer.
	 * 
	 * @throws IOException
	 *             won't happen mock side effect.
	 */
	@Test(expected = ScriptException.class)
	public void ioExceptionInWriter() throws IOException {

		PowerMockito.when(
				IoFactory.writer(Mockito.isA(File.class),
						Mockito.isA(String.class)))
				.thenThrow(new IOException());

		this.scriptWriter.linux();
	}

	/**
	 * Test when there is an IOexception when getting the pre-start script
	 * content.
	 * 
	 * @throws IOException
	 *             won't happen mock side effect.
	 */
	@Test(expected = ScriptException.class)
	public void ioExceptionInContent() throws IOException {

		PowerMockito.mockStatic(FileContent.class);

		PowerMockito.when(FileContent.get(Mockito.anyString())).thenThrow(
				new IOException());

		this.scriptWriter.linux();
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

			try {
				
				Mockito.when(FileContent.get(platform.behave(this.preStart)))
				.thenReturn(preStartContent);
				
			} catch (IOException e) {
				// Won't happen is a mock side effect.
				
			}

			String expected = ScriptTestReader.content("start-"+ platform, postFix);

			Writer writer = FileWriterMock.mock();


			platform.behave(this.scriptWriter);

			Assert.assertEquals("Unexpected start script content.", expected,
					writer.toString());
		}

	}

	
}
