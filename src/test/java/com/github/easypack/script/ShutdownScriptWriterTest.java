package com.github.easypack.script;

import java.io.File;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.easypack.io.FileContent;
import com.github.easypack.io.IoFactory;
import com.github.easypack.mock.FileWriterMock;
import com.github.easypack.platform.Platform;
import com.github.easypack.test.utils.ScriptTestReader;

/**
 * Unit test for {@link ShutdownScriptWriter}.
 * 
 * @author agusmunioz
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IoFactory.class, FileContent.class })
public class ShutdownScriptWriterTest {

	private File folder = new File(System.getProperty("java.io.tmpdir"));

	@Before
	public void init() {

		PowerMockito.mockStatic(IoFactory.class);

		PowerMockito.mockStatic(FileContent.class);
	}

	/**
	 * Test the generation of the shutdown script
	 */
	@Test
	public void write() {

		ShutdownScriptWriter shutdown = new ShutdownScriptWriter().process(
				"my-app").folder(this.folder);

		for (Platform platform : Platform.values()) {

			String expected = ScriptTestReader.content("shutdown-" + platform, "");

			Writer writer = FileWriterMock.mock();

			platform.behave(shutdown);

			Assert.assertEquals("Unexpected start script content.", expected,
					writer.toString());
		}
	}

}
