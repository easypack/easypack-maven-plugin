package com.github.easypack.jar;

import java.util.Arrays;
import java.util.Collection;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Assert;
import org.junit.Test;

import com.github.easypack.io.PathSeparator;
import com.github.easypack.io.PathUtils;

/**
 * Unit test for {@link JarConfiguration}.
 * 
 * @author agusmunioz
 * 
 */
public class JarConfigurationTest {

	/**
	 * Test the manager when configuring default values as no inclusions nor
	 * exclusions specified.
	 */
	@Test
	public void defaultConfiguration() {

		JarConfiguration manager = new JarConfiguration();

		Xpp3Dom configuration = new Xpp3Dom("configuration");

		manager.configure(configuration);

		Assert.assertNull("Includes added when no inclusions where specified",
				configuration.getChild("includes"));

		Xpp3Dom excludes = configuration.getChild("excludes");

		Assert.assertNotNull("No exclusions configured", excludes);

		Assert.assertEquals("Incorrect exclusion of bin folder", "bin"
				+  PathSeparator.get() + "**", excludes.getChild("exclude")
				.getValue());
	}

	/**
	 * Test when inclusions are configured.
	 */
	@Test
	public void inclusions() {

		JarConfiguration manager = new JarConfiguration();

		manager.setIncludes("files/afile.txt, classes/**");

		Xpp3Dom configuration = new Xpp3Dom("configuration");

		manager.configure(configuration);

		Xpp3Dom includes = configuration.getChild("includes");

		Assert.assertNotNull("No inclusions configured", includes);

		Collection<String> values = Arrays.asList(
				PathUtils.osify("files/afile.txt"),
				PathUtils.osify("classes/**"));

		Assert.assertEquals("Incorrect amount of includes", values.size(),
				includes.getChildCount());

		for (Xpp3Dom include : includes.getChildren()) {

			Assert.assertTrue("Unexpected include: " + include.getValue(),
					values.contains(include.getValue()));
		}

		Xpp3Dom excludes = configuration.getChild("excludes");

		Assert.assertNotNull("No exclusions configured", excludes);

		Assert.assertEquals("Incorrect exclusion of bin folder.", "bin"
				+  PathSeparator.get() + "**", excludes.getChild("exclude")
				.getValue());

	}

	/**
	 * Test when exclusions are configured.
	 */
	@Test
	public void exclusions() {

		JarConfiguration manager = new JarConfiguration();

		manager.setExcludes("files/afile.txt, classes/**");

		Xpp3Dom configuration = new Xpp3Dom("configuration");

		manager.configure(configuration);

		Assert.assertNull("Inclusions configured when not expected",
				configuration.getChild("includes"));

		Xpp3Dom excludes = configuration.getChild("excludes");

		Assert.assertNotNull("No exclusions configured", excludes);

		Collection<String> values = Arrays.asList(PathUtils.osify("bin/**"),
				PathUtils.osify("files/afile.txt"),
				PathUtils.osify("classes/**"));

		Assert.assertEquals("Incorrect amount of exclusions", values.size(),
				excludes.getChildCount());

		for (Xpp3Dom exclude : excludes.getChildren()) {

			Assert.assertTrue("Unexpected exclude: " + exclude.getValue(),
					values.contains(exclude.getValue()));
		}
	}
}
