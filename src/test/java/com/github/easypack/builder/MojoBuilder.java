package com.github.easypack.builder;

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

		this.setField(this.getField(object.getClass()), object);

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

		this.setField(this.getField(name), value);

		return this;
	}

	/**
	 * Searches a field recursively.
	 * 
	 * @param name
	 *            the field name.
	 * 
	 * @return the field or null if not found.
	 */
	private Field getField(String name) {

		Class<?> type = this.mojo.getClass();

		while (!type.equals(Object.class)) {

			try {

				Field field = type.getDeclaredField(name);

				if (field != null) {
					return field;
				}

			} catch (NoSuchFieldException | SecurityException e) {

			}

			type = type.getSuperclass();

		}

		return null;
	}

	/**
	 * Searches a field recursively.
	 * 
	 * @param name
	 *            the field name.
	 * 
	 * @return the field or null if not found.
	 */
	private Field getField(Class<?> fieldType) {

		Class<?> type = this.mojo.getClass();

		while (!type.equals(Object.class)) {

			Field[] fields = type.getDeclaredFields();

			for (Field field : fields) {

				if (field.getType().equals(fieldType)) {
					return field;
				}
			}

			type = type.getSuperclass();

		}

		return null;
	}

	/**
	 * Sets the field value.
	 * 
	 * @param field
	 *            the field.
	 * @param value
	 *            the value.
	 */
	private void setField(Field field, Object value) {

		try {

			field.setAccessible(true);
			field.set(this.mojo, value);

		} catch (Exception e) {
		}

	}
}
