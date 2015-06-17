package com.github.agusmunioz.easypack.builder;

import org.apache.maven.model.Plugin;

/**
 * Factory class for building dummy Maven plugins.
 * 
 * @author agusmunioz
 * 
 */
public class MavenPluginBuilder {

	/**
	 * Builds the Maven dependency plugin.
	 * 
	 * @return the plugin
	 */
	public static Plugin dependency() {

		Plugin dependency = new Plugin();
		dependency.setGroupId("org.apache.maven.plugins");
		dependency.setArtifactId("maven-dependency-plugin");
		dependency.setVersion("2.10");

		return dependency;
	}

	/**
	 * Builds the Maven jar plugin.
	 * 
	 * @return the plugin
	 */
	public static Plugin jar() {
		
		Plugin jar = new Plugin();
		jar.setGroupId("org.apache.maven.plugins");
		jar.setArtifactId("maven-jar-plugin");
		jar.setVersion("2.5");

		return jar;
	}
}
