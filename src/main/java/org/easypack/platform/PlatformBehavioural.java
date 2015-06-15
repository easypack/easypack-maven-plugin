package org.easypack.platform;

/**
 * Protocol for behaving based on a {@link Platform}.
 * 
 * @author agusmunioz
 *
 * @param <T>
 *            type of the behavior result.
 */
public interface PlatformBehavioural<T> {

	/**
	 * Behavior for a Linux platform.
	 * 
	 * @return the behavior result.
	 */
	T linux();

	/**
	 * Behavior for Windows platform.
	 * 
	 * @return the behavior result.
	 */
	T windows();
}
