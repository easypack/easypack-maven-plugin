package com.github.easypack.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;

import com.github.easypack.assertions.AssertPackaging;
import com.github.easypack.builder.MavenProjectBuilder;
import com.github.easypack.builder.MojoBuilder;

/**
 * Unit test for {@link ZipPackagerMojo}.
 * 
 * @author agusmunioz
 * 
 */
public class ZipPackagerMojoTest {

	/**
	 * Test a successful packagin of a project.
	 */
	@Test
	public void packaging() {

		MavenProject project = MavenProjectBuilder.tmpProject();

		ZipPackagerMojo mojo = (ZipPackagerMojo) MojoBuilder
				.build(new ZipPackagerMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("finalName", project.getBuild().getFinalName()).get();

		try {

			mojo.execute();

			AssertPackaging.assertPackaged(project);

		} catch (MojoExecutionException | MojoFailureException e) {
			Assert.fail("Unexpected exception " + e);
		}

	}
}
