package com.github.easypack.script;

import java.io.File;

import org.apache.velocity.VelocityContext;

import com.github.easypack.io.PathUtils;
import com.github.easypack.platform.PlatformBehavioural;

/**
 * Writes (creates) the shutdown script to be added in the final artifact.
 * 
 * @author agusmunioz
 * 
 */
public class ShutdownScriptWriter implements PlatformBehavioural<Void> {

	private static final String SHUTDOWN = "shutdown";

	private VelocityContext context;

	private File folder;

	/**
	 * Creates an initialized {@link ShutdownScriptWriter}.
	 */
	public ShutdownScriptWriter() {
		this.context = new VelocityContext();
	}

	/**
	 * Sets the application process name at OS level.
	 * 
	 * @param name
	 *            the process name.
	 * 
	 * @return the script writer for further configuration or writing.
	 */
	public ShutdownScriptWriter process(String name) {
		context.put("process", name);
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
	public ShutdownScriptWriter folder(File folder) {
		this.folder = folder;
		return this;
	}

	@Override
	public Void linux() {
		VelocityUtils.write(VelocityUtils.linux(SHUTDOWN),
				PathUtils.sh(SHUTDOWN), this.folder, this.context);
		return null;
	}

	@Override
	public Void windows() {
		VelocityUtils.write(VelocityUtils.windows(SHUTDOWN),
				PathUtils.bat(SHUTDOWN), this.folder, this.context);
		return null;
	}

}
