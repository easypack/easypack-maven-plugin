package org.easypack.mojo;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Resource;
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

	/**
	 * Known excluded files.
	 */
	private static final String EXCLUSIONS = "/bin/start-linux,/bin/start-windows";

	private File binFolder;

	private MavenProject project;

	private File resourceFolder;

	@Before
	public void init() {

		this.project = MavenProjectBuilder.dummy();

		PowerMockito.mockStatic(FileUtils.class);

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

		PowerMockito.mockStatic(IoFactory.class);

		String target = project.getBuild().getOutputDirectory();

		this.binFolder = FileMock.mock();

		PowerMockito.when(IoFactory.file(target + "/" + FolderConstants.BIN))
				.thenReturn(binFolder);

		Resource resources = this.project.getResources().get(0);

		this.resourceFolder = new File(resources.getDirectory());

		PowerMockito.when(IoFactory.file(resources.getDirectory())).thenReturn(
				this.resourceFolder);
	}

	/**
	 * Test the mojo execution when the project doesn't define a bin folder and
	 * no expression for including files (mojo bin property) is configured.
	 */
	@Test
	public void defaultConfiguration() {

		List<File> files = new LinkedList<File>();

		FileUtilsMock.getFiles(this.resourceFolder, CopyBinFilesMojo.BIN_REGEX,
				EXCLUSIONS, files);

		CopyBinFilesMojo mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("platforms", "linux,windows").with("echo", "").get();

		try {

			mojo.execute();

			FileUtilsMock.verifyNoFilesCopied();

		} catch (MojoExecutionException | MojoFailureException e) {

			Assert.fail("Unexpected exception. " + e);

		}
	}

	/**
	 * Test any file under project src/main/resources/bin folder is copied to
	 * the final bin folder.
	 */
	@Test
	public void filesInBinFolder() {

		CopyBinFilesMojo mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("platforms", "linux,windows").with("echo", "").get();

		List<File> expected = Arrays.asList(new File(
				"src/main/resources/bin/hola.sh"), new File(
				"src/main/resources/bin/chau.sh"));

		FileUtilsMock.getFiles(this.resourceFolder, CopyBinFilesMojo.BIN_REGEX,
				EXCLUSIONS, expected);

		try {

			mojo.execute();

			for (File file : expected) {
				FileUtilsMock.verifyCopied(file, this.binFolder);
			}

		} catch (MojoExecutionException | MojoFailureException e) {

			Assert.fail("Unexpected exception. " + e);

		}
	}

	/**
	 * Test a specific file and a file inside a folder are copied in the final
	 * bin folder.
	 */
	@Test
	public void includes() {

		String[] includes = new String[] { "scripts/hola.sh", "tests/" };

		CopyBinFilesMojo mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("platforms", "linux,windows").with("echo", "")
				.with("bin", includes).get();

		String inclusions = StringUtils.join(includes, ",") + ","
				+ CopyBinFilesMojo.BIN_REGEX;

		List<File> expected = Arrays.asList(new File(
				"src/main/resources/scripts/hola.sh"), new File(
				"src/main/resources/tests/chau.sh"));

		FileUtilsMock.getFiles(this.resourceFolder, inclusions, EXCLUSIONS,
				expected);

		try {

			mojo.execute();

			for (File file : expected) {
				FileUtilsMock.verifyCopied(file, this.binFolder);
			}

		} catch (MojoExecutionException | MojoFailureException e) {

			Assert.fail("Unexpected exception. " + e);

		}
	}
}
