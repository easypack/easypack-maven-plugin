package com.github.easypack.mojo;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.easypack.builder.MavenProjectBuilder;
import com.github.easypack.builder.MojoBuilder;
import com.github.easypack.constants.FolderConstants;
import com.github.easypack.io.IoFactory;
import com.github.easypack.mock.FileMock;
import com.github.easypack.mock.FileUtilsMock;

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

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

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

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

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

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

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

	/**
	 * Test when the resouce folder is configured but doesn't exists.
	 */
	@Test
	public void directoryNotExists() {

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(false);

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
	 * Test when copying files throws an I/O exception.
	 * 
	 * @throws MojoExecutionException
	 *             expected.
	 */
	@Test(expected = MojoExecutionException.class)
	public void copyIoException() throws MojoExecutionException {

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

		List<File> files = Arrays.asList(new File(
				"src/main/resources/bin/hola.sh"), new File(
				"src/main/resources/bin/chau.sh"));

		FileUtilsMock.getFiles(this.resourceFolder, CopyBinFilesMojo.BIN_REGEX,
				EXCLUSIONS, files);

		FileUtilsMock.copyToDirectoryIOException();
		
		CopyBinFilesMojo mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("platforms", "linux,windows").with("echo", "").get();

		try {

			mojo.execute();

		} catch (MojoFailureException e) {
			Assert.fail("Unexpected exception. " + e);
		}
	}

	/**
	 * Test when the filtering of files throws an I/O exception.
	 * 
	 * @throws MojoExecutionException
	 *             expected.
	 */
	@Test(expected = MojoExecutionException.class)
	public void getFileIoException() throws MojoExecutionException {

		PowerMockito.when(FileUtils.fileExists(Mockito.anyString()))
				.thenReturn(true);

		FileUtilsMock.getFilesIOException();

		CopyBinFilesMojo mojo = (CopyBinFilesMojo) MojoBuilder
				.build(new CopyBinFilesMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("platforms", "linux,windows").with("echo", "").get();

		try {

			mojo.execute();

		} catch (MojoFailureException e) {
			Assert.fail("Unexpected exception. " + e);
		}
	}
}
