package com.github.easypack.script;

/**
 * Exception thrown when there is an error writing the scripts.
 * 
 * @author agusmunioz
 * 
 */
public class ScriptException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScriptException(String message, Throwable cause) {
		super(message, cause);

	}

}
