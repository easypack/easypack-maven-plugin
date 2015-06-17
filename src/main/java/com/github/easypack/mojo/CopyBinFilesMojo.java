package com.github.easypack.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import com.github.easypack.constants.FolderConstants;
import com.github.easypack.io.IoFactory;
import com.github.easypack.io.PathUtils;
import com.github.easypack.platform.Platform;
import com.github.easypack.script.PreStart;

/**
 * Mojo that copies files or folders from the project being built, to the bin
 * folder included in the final artifact.
 * 
 * @author agusmunioz
 *
 */
@Mojo(name = "copy")
public class CopyBinFilesMojo extends AbstractMojo {

	/**
	 * Regular expression for including any file in project bin folder.
	 */
	public static final String BIN_REGEX = "bin" + PathUtils.SEPARATOR + "*";

	@Component
	private MavenProject project;

	@Parameter(property = "project.build.directory", readonly = true)
	private String outputDirectory;

	/**
	 * Folder (including scripts) or scripts to be added to the final bin
	 * folder.
	 */
	@Parameter
	private String[] bin = new String[] {};

	/**
	 * The targeted platforms for the project being built. Supported values:
	 * windows, linux.
	 */
	@Parameter(defaultValue = "linux")
	private String platforms;

	/**
	 * Script to be executed before application startup. It is merged in the
	 * final start script. Default names bin/start-linux, bin/start-windows.
	 */
	@Parameter(alias = "start")
	private PreStart preStart = new PreStart();

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		File folder = IoFactory.file(PathUtils.path(this.outputDirectory,
				FolderConstants.BIN));

		List<Resource> resources = this.project.getResources();

		for (Resource resource : resources) {

			if (FileUtils.fileExists(resource.getDirectory())) {
				this.copy(resource, folder);
			}
		}
	}

	/**
	 * Copies all the scripts from project bin folder, or any that matches the
	 * configured regular expressions in {@link CreateScriptsMojo#bin} property.
	 * It excludes files like pre-start scripts.
	 * 
	 * @param resource
	 *            a project resource folder.
	 * 
	 * @param folder
	 *            the destination folder.
	 * 
	 * @throws MojoExecutionException
	 *             if there is an error while searching or copying the found
	 *             scripts.
	 */
	private void copy(Resource resource, File folder)
			throws MojoExecutionException {

		String[] includes = (String[]) ArrayUtils.add(this.bin, BIN_REGEX);

		String[] excludes = this.getExcluded(resource);

		try {

			Collection<File> files = FileUtils.getFiles(
					IoFactory.file(resource.getDirectory()),
					StringUtils.join(includes, ","),
					StringUtils.join(excludes, ","));

			for (File file : files) {

				FileUtils.copyFileToDirectory(file, folder);

			}

		} catch (IOException e) {
			throw new MojoExecutionException("Error when copying scripts.", e);
		}

	}

	/**
	 * Gets a list of regular expressions of the scripts that must not be
	 * included in the final artifact, like pre-start scripts.
	 * 
	 * @param resource
	 *            the project resource folder.
	 * 
	 * @return a not null array of script names regular expression to be
	 *         excluded.
	 */
	private String[] getExcluded(Resource resource) {

		Collection<String> excludes = new LinkedList<String>();

		String relativePath = PathUtils.subtract(resource.getDirectory(),
				this.project.getBasedir().getAbsolutePath()
						+ PathUtils.SEPARATOR);

		for (Platform platform : Platform.fromString(this.platforms)) {

			String preStart = platform.behave(this.preStart);

			excludes.add(PathUtils.subtract(preStart, relativePath));
		}

		return excludes.toArray(new String[] {});
	}
}
