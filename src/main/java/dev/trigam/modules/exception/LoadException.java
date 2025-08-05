package dev.trigam.modules.exception;

public class LoadException extends RuntimeException {
	public LoadException ( String message ) {
		super( message );
	}
	
	public LoadException ( String message, Exception cause ) {
		super( message, cause );
	}
}
