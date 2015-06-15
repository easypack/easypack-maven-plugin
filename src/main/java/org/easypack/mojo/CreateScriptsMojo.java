package org.easypack.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.easypack.constants.FolderConstants;
import org.easypack.io.IoFactory;
import org.easypack.io.PathUtils;
import org.easypack.platform.Platform;
import org.easypack.script.PreStart;
import org.easypack.script.StartScriptWriter;

/**
 * Creates the application scripts to be included in the final artifact.
 * 
 * @author agusmunioz
 * 
 */
@Mojo(name = "scripts")
public class CreateScriptsMojo extends AbstractMojo {

	@Parameter(property = "project.build.directory", readonly = true)
	private String outputDirectory;

	@Parameter(property = "project.build.finalName", readonly = true)
	private String finalName;

	/**
	 * Java Virtual Machine options, like maximum heap size (-Xmx3G) or
	 * environment properties specified with -D.
	 */
	@Parameter(defaultValue = "")
	private String opts = "";

	/**
	 * Arguments passed to the application main method.
	 * 
	 */
	@Parameter(defaultValue = "")
	private String args = "";

	/**
	 * A comma separated list of platforms, the start script must be created
	 * for. Supported values: windows, linux.
	 */
	@Parameter(defaultValue = "linux")
	private String platforms;

	/**
	 * Script/s to be executed before application startup. Will be merged in the
	 * final start script/s. Default names bin/start-linux, bin/start-windows.
	 */
	@Parameter(alias = "start")
	private PreStart preStart = new PreStart();

	/**
	 * Defines which part of the start script must be echoed when executed.
	 * Possible values are: all (for echoing the entire script) or java (for
	 * echoing only the java line). By default no echoing is performed.
	 */
	@Parameter(defaultValue = "")
	private String echo;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File folder = IoFactory.file(PathUtils.path(this.outputDirectory,
				FolderConstants.BIN));

		if (folder.exists()) {
			folder.delete();
		}

		folder.mkdir();

		StartScriptWriter writer = new StartScriptWriter();

		writer.args(this.args).opts(this.opts).echo(this.echo)
				.jar(this.finalName).folder(folder).preStart(this.preStart);

		try {

			for (Platform platform : Platform.fromString(this.platforms)) {

				platform.behave(writer);

			}

		} catch (Exception e) {
			throw new MojoExecutionException(
					"Exception while creating scripts.", e);
		}

	}

}
