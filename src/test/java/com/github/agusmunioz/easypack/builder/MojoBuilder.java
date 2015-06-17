package com.github.agusmunioz.easypack.builder;

import java.lang.reflect.Field;

import org.apache.maven.plugin.AbstractMojo;

/**
 * It helps to build and configure a Mojo.
 * 
 * @author agusmunioz
 *
 */
public class MojoBuilder {

	private AbstractMojo mojo;

	private MojoBuilder(AbstractMojo mojo) {
		this.mojo = mojo;
	}

	/**
	 * Creates a {@link MojoBuilder} with an {@link AbstractMojo} to be
	 * configured.
	 * 
	 * @param mojo
	 *            a mojo to be configured.
	 * 
	 * @return this builder.
	 */
	public static MojoBuilder build(AbstractMojo mojo) {
		return new MojoBuilder(mojo);
	}

	/**
	 * Gets the configured mojo.
	 * 
	 * @return the mojo.
	 */
	public AbstractMojo get() {
		return this.mojo;
	}

	/**
	 * Injects the object in any mojo field with the same declared type.
	 * 
	 * @param object
	 *            the object to be injected in the mojo.
	 * 
	 * @return the builder.
	 */
	public MojoBuilder with(Object object) {

		for (Field field : this.mojo.getClass().getDeclaredFields()) {

			if (field.getType().isAssignableFrom(object.getClass())) {

				field.setAccessible(true);

				try {

					field.set(this.mojo, object);
					return this;

				} catch (Exception e) {
					return this;
				}

			}
		}

		return this;
	}

	/**
	 * Sets the mojo field with the specified value.
	 * 
	 * @param name
	 *            the field name.
	 *            
	 * @param value
	 *            the field value.
	 *            
	 * @return the builder.s
	 */
	public MojoBuilder with(String name, Object value) {

		try {

			Field field = this.mojo.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(this.mojo, value);

		} catch (Exception e) {
		}
		return this;
	}
}
