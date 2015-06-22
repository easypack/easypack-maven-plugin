package com.github.easypack.assertions;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;

/**
 * Assertions for packagins.
 * 
 * @author agusmunioz
 * 
 */
public class AssertPackaging {

	/**
	 * Expected files in the final artifact.
	 */
	private static Set<String> EXPECTED_FILES = new HashSet<String>();

	static {
		EXPECTED_FILES.add("/bin/hola.sh");
		EXPECTED_FILES.add("/bin/chau.sh");
		EXPECTED_FILES.add("/bin/start.sh");
		EXPECTED_FILES.add("/libs/dependency.jar");
	}

	/**
	 * Assert the project has been correctly packaged.
	 * 
	 * @param project
	 *            the packaged projects.
	 */
	public static void assertPackaged(MavenProject project, String format) {

		File artifact = project.getArtifact().getFile();

		Assert.assertTrue("The project artifact has not being created.",
				artifact.exists());

		Set<String> expected = new HashSet<String>();

		for (String file : EXPECTED_FILES) {
			expected.add(project.getBuild().getFinalName() + file);
		}

		expected.add(project.getBuild().getFinalName() + "/libs/"
				+ project.getBuild().getFinalName() + ".jar");

		try (ArchiveInputStream input = getStream(artifact, format)) {

			int packaged = 0;

			ArchiveEntry entry = input.getNextEntry();

			while (entry != null) {

				Assert.assertTrue("Unexpected file has been packaged.",
						expected.contains(entry.getName()));
				entry = input.getNextEntry();
				packaged++;

			}

			Assert.assertEquals("Unexpected amount of packaged files.",
					expected.size(), packaged);

		} catch (IOException | ArchiveException e) {
			Assert.fail("Unexpected exception when asserting on the packaging"
					+ e);
		}
	}

	private static ArchiveInputStream getStream(File file, String format)
			throws MalformedURLException, IOException, ArchiveException {

		if ("GZip".equals(format)) {

			return new TarArchiveInputStream(new GzipCompressorInputStream(file
					.toURI().toURL().openStream()));
		}

		return new ArchiveStreamFactory().createArchiveInputStream(file.toURI()
				.toURL().openStream());

	}

}
