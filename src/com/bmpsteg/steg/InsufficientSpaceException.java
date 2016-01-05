package com.bmpsteg.steg;

/**
 * Exception thrown when data to hide is bigger than steganography method could
 * hide into image.
 * 
 * @author irelic
 *
 */
public class InsufficientSpaceException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsufficientSpaceException(String message) {
		super(message);
	}

	public InsufficientSpaceException() {
		super();
	}
}
