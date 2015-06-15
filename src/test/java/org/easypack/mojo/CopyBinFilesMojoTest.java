package org.easypack.mojo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.easypack.constants.FolderConstants;
import org.easypack.io.IoFactory;
import org.easypack.test.builder.MavenProjectBuilder;
import org.easypack.test.builder.MojoBuilder;
import org.easypack.test.mock.FileMock;
import org.easypack.test.mock.FileUtilsMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for {@link CopyBinFilesMojo}.
 * 
 * @author agusmunioz
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IoFactory.class, FileUtils.class })
public class CopyBinFilesMojoTest {

	private CopyBinFilesMojo mojo;

	private File binFolder;

	@Before
	public void init() {

		MavenProject project = MavenProjectBuilder.dummy();

		String target = project.getBuild().getOutputDirectory();

		this.mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo()).with(project)
				.with("outputDirectory", target)
				.with("platforms", "linux,windows").with("echo", "").get();

		PowerMockito.mockStatic(FileUtils.class);

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

		PowerMockito.mockStatic(IoFactory.class);

		this.binFolder = FileMock.mock();

		PowerMockito.when(IoFactory.file(target + "/" + FolderConstants.BIN))
				.thenReturn(binFolder);
	}

	/**
	 * Test the mojo execution when the project doesn't define a bin folder and
	 * no expression for including files (mojo bin property) is configured.
	 */
	@Test
	public void defaultConfiguration() {

		List<File> files = new LinkedList<File>();

		FileUtilsMock.getFiles(null, CopyBinFilesMojo.BIN_REGEX, "", files);		

		try {

			this.mojo.execute();
			
			FileUtilsMock.verifyNoFilesCopied();

		} catch (MojoExecutionException | MojoFailureException e) {

			Assert.fail("Unexpected exception. " + e);

		}
	}
}
