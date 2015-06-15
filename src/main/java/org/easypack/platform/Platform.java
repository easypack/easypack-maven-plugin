package org.easypack.platform;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Models the project targeted platform (operating system).
 * 
 * @author agusmunioz
 * 
 */
public enum Platform {

	WINDOWS {

		@Override
		public <T> T behave(PlatformBehavioural<T> behavioural) {
			return behavioural.windows();

		}
	},
	LINUX {
		@Override
		public <T> T behave(PlatformBehavioural<T> behavioural) {
			return behavioural.linux();

		}
	};

	/**
	 * Triggers a behavior based on the specific platform.
	 * 
	 * @param behavioural
	 *            the one interested in behaving based on the platform.
	 * 
	 * @return the result of behaving based on the platform.
	 */
	public abstract <T> T behave(PlatformBehavioural<T> behavioural);

	/**
	 * Gets a list of {@link Platform} using a comma separated string.
	 * 
	 * @param platforms
	 *            a comma separated list of platforms.
	 * 
	 * @return the list of {@link Platform}.
	 * 
	 * @throws IllegalArgumentException
	 *             if a not supported platform name is provided.
	 */
	public static Collection<Platform> fromString(String platforms) {

		Collection<Platform> mapped = new LinkedList<Platform>();

		String[] selected = platforms.split(",");

		for (String platform : selected) {

			try {

				mapped.add(Platform.valueOf(platform.trim().toUpperCase()));

			} catch (IllegalArgumentException e) {

				throw new IllegalArgumentException("Unsupported platform: "
						+ platform + ". Supported: linux, windows");
			}

		}

		return mapped;
	}
}
