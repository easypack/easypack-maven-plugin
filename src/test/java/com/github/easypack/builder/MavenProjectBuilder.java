package com.github.easypack.builder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

import com.github.easypack.mock.ArtifactMock;

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

	/**
	 * Builds a dummy project in the file system temporary folder, with folders
	 * and file for being packaged.
	 * 
	 * @return the project.
	 */
	public static MavenProject tmpProject() {

		MavenProject project = new MavenProject();
		project.setGroupId("org.easypack.tests");
		project.setArtifactId("easy-pack-test-project");
		project.setVersion("1.0");

		File folder = new File(System.getProperty("java.io.tmpdir"),
				"easy-pack-test-project");

		if (folder.exists()) {
			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		folder.mkdir();
		project.setFile(folder);
		project.setArtifact(new ArtifactMock());

		File target = new File(folder, "target");
		target.mkdir();

		Build build = new Build();
		build.setDirectory(folder.getAbsolutePath());
		build.setOutputDirectory(target.getAbsolutePath());
		build.setFinalName("mock-project-" + project.getVersion());
		project.setBuild(build);

		File resourceFolder = new File(folder, "src/main/resources");

		try {
			FileUtils.forceMkdir(resourceFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Resource resource = new Resource();
		resource.setDirectory(resourceFolder.getAbsolutePath());
		project.addResource(resource);

		File binFolder = new File(target, "bin");
		binFolder.mkdir();

		String[] scripts = new String[] { "hola.sh", "chau.sh" };

		for (String script : scripts) {
			File file = new File(binFolder, script);
			try {
				FileUtils.touch(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		File bash = new File(binFolder, "start.sh");
		
		try {
			FileUtils.touch(bash);
			FileUtils.write(bash, "#!/bin/bash");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		File libsFolder = new File(target, "libs");
		libsFolder.mkdir();

		String[] jars = new String[] {
				project.getBuild().getFinalName() + ".jar", "dependency.jar" };

		for (String name : jars) {
			File jar = new File(libsFolder, name);
			try {
				FileUtils.touch(jar);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return project;
	}
}
