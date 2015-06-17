package com.github.easypack.script;

/**
 * Sanitizes parameters configured through the pom.xml and used in the scripts
 * created by the plugin.
 * 
 * @author agusmunioz
 * 
 */
public class ScriptSanitizer {

	private static final String NEW_LINE_REGEX = "\\r\\n|\\r|\\n";

	private static final String TAB_REGEX = "\\t";

	private ScriptSanitizer() {
		throw new IllegalAccessError("Constuctor cannot be called, "
				+ "not even with reflection.");
	}

	/**
	 * Removes new line and tab characters.
	 * 
	 * @param parameters
	 *            the String containing parameters for the script.
	 * 
	 * @return the purged arguments line.
	 */
	public static String sanitize(String parameters) {

		StringBuilder builder = new StringBuilder();

		String[] lines = parameters.split(NEW_LINE_REGEX);

		for (String line : lines) {

			builder.append(" ").append(line.replaceAll(TAB_REGEX, "").trim());
		}

		return builder.toString().trim();
	}
}
