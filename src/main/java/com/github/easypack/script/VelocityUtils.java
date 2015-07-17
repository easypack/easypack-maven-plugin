package com.github.easypack.script;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.github.easypack.io.IoFactory;
import com.github.easypack.io.PathUtils;

/**
 * A utility class for helping with Velocity.
 * 
 * @author agusmunioz
 * 
 */
public class VelocityUtils {

	/**
	 * Templates location in the classpath.
	 */
	private static final String TEMPLATES = "/templates/";

	static {

		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				ClasspathResourceLoader.class.getCanonicalName());

		Velocity.init(p);
	}

	/**
	 * Writes the file in the file-system.
	 * 
	 * @param template
	 *            the template name.
	 * 
	 * @param name
	 *            the file final name.
	 * 
	 * @param folder
	 *            the file destination folder.
	 * 
	 * @param context
	 *            the velocity context.
	 * 
	 * @throws ScriptException
	 *             if there is an error while creating the file.
	 */
	public static void write(String template, String name, File folder,
			VelocityContext context) {

		try (Writer writer = IoFactory.writer(folder, name)) {

			getTemplate(template).merge(context, writer);

		} catch (IOException e) {

			throw new ScriptException(
					"Un error occurred while creating the script " + name, e);
		}

	}

	/**
	 * Gets the velocity template.
	 * 
	 * @param name
	 *            the template name.
	 * 
	 * @return the template.
	 */
	private static Template getTemplate(String name) {

		return Velocity.getTemplate(TEMPLATES + PathUtils.file("vm", name),
				"UTF-8");
	}

	/**
	 * Gets the name for Linux templates.
	 * 
	 * @param name
	 *            the template name.
	 * 
	 * @return the template name for Linux
	 */
	public static String linux(String name) {

		return name + "-linux";
	}

	/**
	 * Gets the name for Windows templates.
	 * 
	 * @param name
	 *            the template name.
	 * 
	 * @return the template name for Windows
	 */
	public static String windows(String name) {

		return name + "-windows";
	}
}
