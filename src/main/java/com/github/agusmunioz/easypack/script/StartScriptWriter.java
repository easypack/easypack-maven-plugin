package com.github.agusmunioz.easypack.script;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.github.agusmunioz.easypack.constants.FolderConstants;
import com.github.agusmunioz.easypack.io.FileContent;
import com.github.agusmunioz.easypack.io.IoFactory;
import com.github.agusmunioz.easypack.io.PathUtils;
import com.github.agusmunioz.easypack.platform.PlatformBehavioural;

/**
 * Writes (creates) the start script to be added in the final artifact.
 * 
 * @author agusmunioz
 *
 */
public class StartScriptWriter implements PlatformBehavioural<Void> {

	static {

		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				ClasspathResourceLoader.class.getCanonicalName());

		Velocity.init(p);
	}

	private static final String TEMPLATES = "/templates";

	private static final String EXTENSION = ".vm";

	private static final String START = "start";

	private VelocityContext context;

	private File folder;

	private PreStart preStart;

	private String jar;

	/**
	 * Creates an intialized {@link StartScriptWriter}.
	 */
	public StartScriptWriter() {
		this.context = new VelocityContext();
	}

	@Override
	public Void linux() {
		context.put("jar", FolderConstants.LIBS + "/" + jar);
		this.write("linux", this.preStart.linux(), "sh");
		return null;
	}

	@Override
	public Void windows() {
		context.put("jar", FolderConstants.LIBS + "\\" + jar);
		this.write("windows", this.preStart.windows(), "bat");
		return null;
	}

	/**
	 * Writes the start script in the filesystem.
	 * 
	 * @param name
	 *            the template post-fix.
	 * 
	 * @param preStart
	 *            the pre-start script path to embed in the start script.
	 * 
	 * @param extension
	 *            the script extension.
	 * 
	 * @throws RuntimeException
	 *             if there is an error while creating the file.
	 */
	private void write(String name, String preStart, String extension) {

		Template template = Velocity.getTemplate(TEMPLATES + "/" + START + "-"
				+ name + EXTENSION, "UTF-8");

		try (Writer writer = IoFactory.writer(folder,
				PathUtils.file(extension, START))) {

			context.put("preStart", FileContent.get(preStart));

			template.merge(context, writer);

		} catch (IOException e) {

			throw new RuntimeException(
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
		this.jar = jar + ".jar";
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
