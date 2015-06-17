package com.github.easypack.mojo;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

import com.github.easypack.constants.FolderConstants;
import com.github.easypack.jar.JarConfiguration;

/**
 * Generates the application libraries by copying all runtime dependencies and
 * creating a jar with the project sources.
 * 
 * @author agusmunioz
 * 
 */
@Mojo(name = "libs", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class LibsMojo extends AbstractMojo {

	private static final String LIBS = "${project.build.directory}/"
			+ FolderConstants.LIBS;

	@Component
	private MavenProject project;

	@Component
	private MavenSession session;

	@Component
	private BuildPluginManager pluginManager;

	/**
	 * Application main class, invoked at startup. Default value is
	 * ${project.groupId}.start.Start
	 */
	@Parameter(defaultValue = "${project.groupId}.start.Start")
	private String mainClass;

	/**
	 * Resources to be included or/and excluded in the project final jar.
	 */
	@Parameter(property = "jar", alias = "jar")
	private JarConfiguration jarConfiguration = new JarConfiguration();

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		this.dependencies();

		this.jar();
	}

	/**
	 * Copies all project dependencies in {@link LibsMojo#LIBS} folder.
	 * 
	 * @throws MojoExecutionException
	 *             if there is an error executing copy-dependencies goal.
	 */
	private void dependencies() throws MojoExecutionException {

		MojoExecutor.executeMojo(MojoExecutor.plugin(
				MojoExecutor.groupId("org.apache.maven.plugins"),
				MojoExecutor.artifactId("maven-dependency-plugin"),
				MojoExecutor.version("2.10")), MojoExecutor
				.goal("copy-dependencies"), MojoExecutor
				.configuration(MojoExecutor.element(
						MojoExecutor.name("outputDirectory"), LIBS)),
				MojoExecutor.executionEnvironment(project, session,
						pluginManager));
	}

	/**
	 * Generates the project jar in {@link LibsMojo#LIBS} folder.
	 * 
	 * @throws MojoExecutionException
	 *             if there is an error executing jar goal.
	 */
	private void jar() throws MojoExecutionException {

		Element manifest = MojoExecutor.element(MojoExecutor.name("manifest"),
				MojoExecutor.element("addClasspath", "true"),
				MojoExecutor.element("mainClass", this.mainClass));

		Xpp3Dom configuration = MojoExecutor.configuration(MojoExecutor
				.element(MojoExecutor.name("outputDirectory"), LIBS),
				MojoExecutor.element(MojoExecutor.name("archive"), manifest));

		this.jarConfiguration.configure(configuration);

		MojoExecutor.executeMojo(MojoExecutor.plugin(
				MojoExecutor.groupId("org.apache.maven.plugins"),
				MojoExecutor.artifactId("maven-jar-plugin"),
				MojoExecutor.version("2.5")), MojoExecutor.goal("jar"),
				configuration, MojoExecutor.executionEnvironment(project,
						session, pluginManager));
	}

}
