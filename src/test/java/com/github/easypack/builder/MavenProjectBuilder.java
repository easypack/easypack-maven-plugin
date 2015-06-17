package com.github.easypack.builder;

import java.io.File;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

/**
 * Factory class for dummy Maven projects
 * 
 * @author agusmunioz
 * 
 */
public class MavenProjectBuilder {

	/**
	 * Builds a dummy project.
	 * 
	 * @return the project.
	 */
	public static MavenProject dummy() {

		MavenProject project = new MavenProject();
		project.setGroupId("org.easypack.tests");
		project.setArtifactId("mock-project");
		project.setVersion("1.0");

		String path = "/home/easypack/mock-project";

		File projectFolder = Mockito.mock(File.class);
		Mockito.when(projectFolder.getAbsolutePath()).thenReturn(path);

		File file = Mockito.mock(File.class);
		Mockito.when(file.getParentFile()).thenReturn(projectFolder);

		project.setFile(file);

		Build build = new Build();
		build.setDirectory(path);
		build.setOutputDirectory(path + "/target");
		build.setFinalName("mock-project-" + project.getVersion());
		project.setBuild(build);

		Resource resource = new Resource();
		resource.setDirectory(path + "/src/main/resources");
		project.addResource(resource);

		return project;
	}
}
