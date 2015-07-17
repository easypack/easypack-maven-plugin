package com.github.easypack.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.easypack.builder.MojoBuilder;
import com.github.easypack.constants.FolderConstants;
import com.github.easypack.io.IoFactory;
import com.github.easypack.io.PathSeparator;
import com.github.easypack.mock.FileMock;
import com.github.easypack.script.PreStart;
import com.github.easypack.script.ShutdownScriptWriter;
import com.github.easypack.script.StartScriptWriter;

/**
 * Unit test for {@link CreateScriptsMojo}.
 * 
 * @author agusmunioz
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IoFactory.class, CreateScriptsMojo.class,
		ShutdownScriptWriter.class })
public class CreateScriptsMojoTest {

	private static final String TARGET = PathSeparator.get() + "target";

	private CreateScriptsMojo mojo;

	private StartScriptWriter start;

	private ShutdownScriptWriter shutdown;

	/**
	 * Initializes each test with common mocking.
	 * 
	 * @throws Exception
	 */
	@Before
	public void init() throws Exception {

		PowerMockito.mockStatic(IoFactory.class);

		this.mojo = (CreateScriptsMojo) MojoBuilder
				.build(new CreateScriptsMojo()).with("outputDirectory", TARGET)
				.with("finalName", "myProject")
				.with("platforms", "linux,windows").with("echo", "").get();

		this.start = PowerMockito.mock(StartScriptWriter.class);

		PowerMockito.whenNew(StartScriptWriter.class).withNoArguments()
				.thenReturn(this.start);

		PowerMockito.when(this.start.args(Mockito.anyString())).thenReturn(
				this.start);

		PowerMockito.when(this.start.opts(Mockito.anyString())).thenReturn(
				this.start);

		PowerMockito.when(this.start.echo(Mockito.anyString())).thenReturn(
				this.start);

		PowerMockito.when(this.start.jar(Mockito.anyString())).thenReturn(
				this.start);

		PowerMockito.when(this.start.folder(Mockito.any(File.class)))
				.thenReturn(this.start);

		PowerMockito.when(this.start.process(Mockito.anyString())).thenReturn(
				this.start);

		PowerMockito.when(this.start.preStart(Mockito.any(PreStart.class)))
				.thenReturn(this.start);

		this.shutdown = PowerMockito.mock(ShutdownScriptWriter.class);

		PowerMockito.whenNew(ShutdownScriptWriter.class).withNoArguments()
				.thenReturn(this.shutdown);

		PowerMockito.when(this.shutdown.process(Mockito.anyString()))
				.thenReturn(this.shutdown);

		PowerMockito.when(this.shutdown.folder(Mockito.any(File.class)))
				.thenReturn(this.shutdown);
	}

	/**
	 * Test the mojo execution when bin folder doesn't exists.
	 */
	@Test
	public void noBinFolder() {

		try {

			File binFolder = FileMock.mock(false);

			PowerMockito.when(
					IoFactory.file(TARGET + PathSeparator.get()
							+ FolderConstants.BIN)).thenReturn(binFolder);

			this.mojo.execute();

			Mockito.verify(binFolder, Mockito.never()).delete();

			Mockito.verify(binFolder, Mockito.times(1)).mkdir();

			this.verifyWriter();

		} catch (Exception e) {
			Assert.fail("Unexpected exception. " + e);
		}

	}

	/**
	 * Test the mojo execution when bin folder exists.
	 */
	@Test
	public void binFolder() {

		try {

			File binFolder = FileMock.mock(true);

			PowerMockito.when(
					IoFactory.file(TARGET + PathSeparator.get()
							+ FolderConstants.BIN)).thenReturn(binFolder);

			this.mojo.execute();

			Mockito.verify(binFolder, Mockito.times(1)).delete();

			Mockito.verify(binFolder, Mockito.times(1)).mkdir();

			this.verifyWriter();

		} catch (Exception e) {
			Assert.fail("Unexpected exception. " + e);
		}

	}

	/**
	 * Test the mojo exception handling.
	 * 
	 * @throws MojoFailureException
	 * 
	 * @throws MojoExecutionException
	 */
	@Test(expected = MojoExecutionException.class)
	public void exception() throws MojoExecutionException, MojoFailureException {

		File binFolder = FileMock.mock();

		PowerMockito.when(
				IoFactory.file(TARGET + PathSeparator.get()
						+ FolderConstants.BIN)).thenReturn(binFolder);

		PowerMockito.when(this.start.linux()).thenThrow(
				new RuntimeException(""));

		this.mojo.execute();

	}

	/**
	 * Verifies if the {@link StartScriptWriter} is called as expected.
	 */
	private void verifyWriter() {

		Mockito.verify(this.start, Mockito.times(1)).linux();

		Mockito.verify(this.start, Mockito.times(1)).windows();
		
		Mockito.verify(this.shutdown, Mockito.times(1)).linux();

		Mockito.verify(this.shutdown, Mockito.times(1)).windows();
	}
}
