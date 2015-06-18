package com.github.easypack.script;

import com.github.easypack.io.PathSeparator;
import com.github.easypack.io.PathUtils;
import com.github.easypack.platform.PlatformBehavioural;

/**
 * Configuration of pre-start scripts, incorporated in the final start script.
 * 
 * @author agusmunioz
 * 
 */
public class PreStart implements PlatformBehavioural<String> {

	private static final String BIN_FOLDER = PathUtils.path("src", "main",
			"resources", "bin" +  PathSeparator.get());

	public static final String DEFAULT_WINDOWS = BIN_FOLDER + "start-windows";

	public static final String DEFAULT_LINUX = BIN_FOLDER + "start-linux";

	private String linux = DEFAULT_LINUX;

	private String windows = DEFAULT_WINDOWS;

	/**
	 * Sets the Linux pre-start script path.
	 * 
	 * @param linux
	 *            a String with the path information.
	 */
	public void setLinux(String linux) {
		this.linux = PathUtils.osify(linux);
	}

	/**
	 * Sets the Windows pre-start script path.
	 * 
	 * @param windows
	 *            a String with the path information.
	 */
	public void setWindows(String windows) {
		this.windows = PathUtils.osify(windows);
	}

	@Override
	public String linux() {
		return this.linux;
	}

	@Override
	public String windows() {
		return this.windows;
	}

}
