package com.github.easypack.script;

import java.io.File;
import java.io.IOException;

import org.apache.velocity.VelocityContext;

import com.github.easypack.constants.FolderConstants;
import com.github.easypack.io.FileContent;
import com.github.easypack.io.PathUtils;
import com.github.easypack.platform.PlatformBehavioural;

/**
 * Writes (creates) the start script to be added in the final artifact.
 * 
 * @author agusmunioz
 * 
 */
public class StartScriptWriter implements PlatformBehavioural<Void> {

	private static final String START = "start";

	private VelocityContext context;

	private File folder;

	private PreStart preStart;

	private String jar;

	private String separator;

	/**
	 * Creates an initialized {@link StartScriptWriter}.
	 */
	public StartScriptWriter() {
		this.context = new VelocityContext();
	}

	@Override
	public Void linux() {

		this.separator = "/";

		this.write(VelocityUtils.linux(START), PathUtils.sh(START),
				this.preStart.linux());

		return null;
	}

	@Override
	public Void windows() {

		this.separator = "\\";

		this.write(VelocityUtils.windows(START), PathUtils.bat(START),
				this.preStart.windows());

		return null;
	}

	/**
	 * Writes the file in the file system.
	 * 
	 * @param template
	 *            the velocity template name.
	 * 
	 * @param name
	 *            the name of the final file.
	 * 
	 * @param preStart
	 *            the pre-start script path.
	 */
	private void write(String template, String name, String preStart) {

		try {

			context.put("jar", FolderConstants.LIBS + this.separator + this.jar);
			context.put("preStart", FileContent.get(preStart));

			VelocityUtils.write(template, name, this.folder, this.context);

		} catch (IOException e) {
			throw new ScriptException(
					"Un error occurred while creating the start scrtip.", e);
		}
	}

	/**
	 * Configures the jar name.
	 * 
	 * @param jar
	 *            the jar name to be used in the script.
	 * 
	 * @return the script for further configuration or writing.
	 */
	public StartScriptWriter jar(String jar) {
		this.jar = PathUtils.file("jar", jar);
		return this;
	}

	/**
	 * Configures the echo strategy. Possible values are: all (for echoing the
	 * entire script) or java (for echoing only the java line). By default no
	 * echoing is performed.
	 * 
	 * @param echo
	 *            the echo strategy to be used in the script.
	 * 
	 * @return the script for further configuration or writing.
	 */
	public StartScriptWriter echo(String echo) {
		context.put("echo", echo);
		return this;
	}

	/**
	 * Configures Java options to be used in the script.
	 * 
	 * @param opts
	 *            a list of Java options as an String.
	 * 
	 * @return the script for further configuration or writing.
	 */
	public StartScriptWriter opts(String opts) {
		context.put("opts", ScriptSanitizer.sanitize(opts));
		return this;
	}

	/**
	 * Configures Java arguments to be used in the script.
	 * 
	 * @param args
	 *            a list string used as the application arguments.
	 * 
	 * @return the script for further configuration or writing.
	 */
	public StartScriptWriter args(String args) {
		context.put("args", ScriptSanitizer.sanitize(args));
		return this;
	}

	/**
	 * The folder where to write the scripts.
	 * 
	 * @param folder
	 *            an existing folder.
	 * 
	 * @return the script writer for further configuration or writing.
	 */
	public StartScriptWriter folder(File folder) {
		this.folder = folder;
		return this;
	}

	/**
	 * Sets the application process name at OS level.
	 * 
	 * @param name
	 *            the process name.
	 * 
	 * @return the script writer for further configuration or writing.
	 */
	public StartScriptWriter process(String name) {
		context.put("process", name);
		return this;
	}

	/**
	 * The pre-start scripts configuration for merging them in the start script.
	 * 
	 * @param startConfiguration
	 *            the start configuration with the paths.
	 * 
	 * @return the script writer for further configuration or writing.
	 */
	public StartScriptWriter preStart(PreStart startConfiguration) {
		this.preStart = startConfiguration;
		return this;
	}

}
