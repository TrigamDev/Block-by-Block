package dev.trigam.modules.exception;

public class DiscoveryException extends RuntimeException {
	public DiscoveryException ( String message ) {
		super( message );
	}
	
	public DiscoveryException ( String message, Exception cause ) {
		super( message, cause );
	}
}
