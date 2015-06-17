package com.github.easypack.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.easypack.constants.FolderConstants;
import com.github.easypack.io.IoFactory;
import com.github.easypack.io.PathUtils;

/**
 * Abstract Mojo that packages the project.
 * 
 * @author agusmunioz
 * 
 */
public abstract class AbstractPackagerMojo extends AbstractMojo {

	private static final String SLASH = "/";

	/**
	 * Folders created by this plugin.
	 */
	private static String[] FOLDERS = new String[] { FolderConstants.LIBS,
			FolderConstants.BIN };

	@Component
	private MavenProject project;

	@Parameter(property = "project.build.directory", readonly = true)
	private String outputDirectory;

	@Parameter(property = "project.build.finalName", readonly = true)
	private String finalName;

	/**
	 * A list of folders or files to be included in the final artifact archive.
	 */
	@Parameter
	private File[] includes;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		File artifact = IoFactory.file(PathUtils.file(this.getExtension(),
				this.outputDirectory, this.finalName));

		this.project.getArtifact().setFile(artifact);

		try (ArchiveOutputStream archive = this.getStream(artifact)) {

			File[] files = this.artifactFiles();

			this.add(archive, this.finalName + SLASH, files);

		} catch (Exception e) {

			throw new MojoExecutionException(
					"Exception when creating artifact archive.", e);
		}
	}

	/**
	 * Lists the folders and files to be included in the final artifact.
	 * 
	 * @return a not empty array of folder and files.
	 */
	private File[] artifactFiles() {

		File[] required = IoFactory.files(this.outputDirectory, FOLDERS);

		return (File[]) ArrayUtils.addAll(required, this.includes);
	}

	/**
	 * Adds files in an archive.
	 * 
	 * @param archive
	 *            the archive where to add files.
	 * 
	 * @param path
	 *            the path where to add each file inside the archive.
	 * 
	 * @param files
	 *            a list of files to be added.
	 * 
	 * @throws IOException
	 *             if there is an error adding any file to the archive.
	 */
	private void add(ArchiveOutputStream archive, String path, File... files)
			throws IOException {

		for (File file : files) {

			if (!file.exists()) {
				throw new FileNotFoundException("Folder or file not found: "
						+ file.getPath());
			}

			String name = path + file.getName();

			if (file.isDirectory()) {

				this.add(archive, name + SLASH, file.listFiles());

			} else {

				ArchiveEntry entry = this.entry(file, name);
				archive.putArchiveEntry(entry);
				IOUtils.copy(new FileInputStream(file), archive);
				archive.closeArchiveEntry();

			}

		}
	}

	/**
	 * Gets the final artifact archive extension.
	 * 
	 * @return a not null nor empty file extension.
	 */
	protected abstract String getExtension();

	/**
	 * Gets an {@link ArchiveOutputStream} for adding all artifact files.
	 * 
	 * @param artifact
	 *            the artifact final file.
	 * 
	 * @return an {@link ArchiveOutputStream} implementation.
	 * 
	 * @throws IOException
	 *             if there is an error creating the {@link ArchiveOutputStream}
	 */
	protected abstract ArchiveOutputStream getStream(File artifact)
			throws IOException;

	/**
	 * Creates an archive entry.
	 * 
	 * @param file
	 *            the file to add as the final archive entry.
	 * 
	 * @param name
	 *            final name in the archive file.
	 * 
	 * @return a specific {@link ArchiveEntry}.
	 */
	protected abstract ArchiveEntry entry(File file, String name);

}
