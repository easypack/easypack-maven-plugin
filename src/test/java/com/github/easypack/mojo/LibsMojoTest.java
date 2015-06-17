package com.github.easypack.mojo;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;

import com.github.easypack.builder.MavenPluginBuilder;
import com.github.easypack.builder.MavenProjectBuilder;
import com.github.easypack.builder.MojoBuilder;
import com.github.easypack.jar.JarConfiguration;
import com.github.easypack.mojo.LibsMojo;

/**
 * Unit test for {@link LibsMojo}.
 * 
 * @author agusmunioz
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MojoExecutor.class)
public class LibsMojoTest {

	/**
	 * Test the successful execution of the Mojo.
	 */
	@Test
	public void successful() {

		MavenProject project = MavenProjectBuilder.dummy();

		MavenSession session = Mockito.mock(MavenSession.class);

		BuildPluginManager pluginManager = Mockito
				.mock(BuildPluginManager.class);

		ExecutionEnvironment environment = new ExecutionEnvironment(project,
				session, pluginManager);

		JarConfiguration jarManger = Mockito.mock(JarConfiguration.class);

		LibsMojo mojo = (LibsMojo) MojoBuilder.build(new LibsMojo())
				.with(project).with(session).with(pluginManager)
				.with("mainClass", "org.easypack.test.start.Start")
				.with(jarManger).get();

		PowerMockito.mockStatic(MojoExecutor.class);

		Plugin dependency = MavenPluginBuilder.dependency();

		this.mock(dependency, "copy-dependencies", environment);

		Plugin jar = MavenPluginBuilder.jar();

		this.mock(jar, "jar", environment);

		try {

			mojo.execute();

			PowerMockito.verifyStatic(Mockito.times(1));

			MojoExecutor.executeMojo(dependency, "copy-dependencies", null,
					environment);

			PowerMockito.verifyStatic(Mockito.times(1));

			MojoExecutor.executeMojo(jar, "jar", null, environment);

		} catch (Exception e) {

			e.printStackTrace();

			Assert.fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Test the mojo fails when dependency plugin fails
	 * 
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	@Test(expected = MojoExecutionException.class)
	public void dependencyFails() throws MojoExecutionException,
			MojoFailureException {

		MavenProject project = MavenProjectBuilder.dummy();

		MavenSession session = Mockito.mock(MavenSession.class);

		BuildPluginManager pluginManager = Mockito
				.mock(BuildPluginManager.class);

		ExecutionEnvironment environment = new ExecutionEnvironment(project,
				session, pluginManager);

		JarConfiguration jarManger = Mockito.mock(JarConfiguration.class);

		LibsMojo mojo = (LibsMojo) MojoBuilder.build(new LibsMojo())
				.with(project).with(session).with(pluginManager)
				.with("mainClass", "org.easypack.test.start.Start")
				.with(jarManger).get();

		PowerMockito.mockStatic(MojoExecutor.class);

		Plugin dependency = MavenPluginBuilder.dependency();

		this.mock(dependency, "copy-dependencies", environment);

		PowerMockito.doThrow(new MojoExecutionException("")).when(
				MojoExecutor.class);

		MojoExecutor.executeMojo(dependency, "copy-dependencies", null,
				environment);

		mojo.execute();

		Assert.fail("Failure expected");
	}

	/**
	 * Test the mojo fails when jar plugin fails
	 * 
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	@Test(expected = MojoExecutionException.class)
	public void jarFails() throws MojoExecutionException, MojoFailureException {

		MavenProject project = MavenProjectBuilder.dummy();

		MavenSession session = Mockito.mock(MavenSession.class);

		BuildPluginManager pluginManager = Mockito
				.mock(BuildPluginManager.class);

		ExecutionEnvironment environment = new ExecutionEnvironment(project,
				session, pluginManager);

		JarConfiguration jarManger = Mockito.mock(JarConfiguration.class);

		LibsMojo mojo = (LibsMojo) MojoBuilder.build(new LibsMojo())
				.with(project).with(session).with(pluginManager)
				.with("mainClass", "org.easypack.test.start.Start")
				.with(jarManger).get();

		PowerMockito.mockStatic(MojoExecutor.class);

		Plugin dependency = MavenPluginBuilder.dependency();

		this.mock(dependency, "copy-dependencies", environment);

		Plugin jar = MavenPluginBuilder.jar();

		this.mock(jar, "jar", environment);

		PowerMockito.doThrow(new MojoExecutionException("")).when(
				MojoExecutor.class);

		MojoExecutor.executeMojo(jar, "jar", null, environment);

		mojo.execute();

		Assert.fail("Failure expected");
	}

	/**
	 * Mocks all {@link MojoExecutor} interaction with the plugin.
	 * 
	 * @param plugin
	 *            a plugin.
	 * 
	 * @param goal
	 *            the goal to be executed.
	 * 
	 * @param environment
	 *            the execution environment.
	 */
	private void mock(Plugin plugin, String goal,
			ExecutionEnvironment environment) {

		PowerMockito.when(MojoExecutor.groupId(plugin.getGroupId()))
				.thenReturn(plugin.getGroupId());

		PowerMockito.when(MojoExecutor.artifactId(plugin.getArtifactId()))
				.thenReturn(plugin.getArtifactId());

		PowerMockito.when(MojoExecutor.version(plugin.getVersion()))
				.thenReturn(plugin.getVersion());

		PowerMockito.when(
				MojoExecutor.plugin(plugin.getGroupId(),
						plugin.getArtifactId(), plugin.getVersion()))
				.thenReturn(plugin);

		PowerMockito.when(MojoExecutor.goal(goal)).thenReturn(goal);

		PowerMockito.when(
				MojoExecutor.executionEnvironment(
						environment.getMavenProject(),
						environment.getMavenSession(),
						environment.getPluginManager()))
				.thenReturn(environment);
	}
}
