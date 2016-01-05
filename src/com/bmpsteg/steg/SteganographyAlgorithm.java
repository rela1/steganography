package com.bmpsteg.steg;

import java.awt.image.BufferedImage;

/**
 * Represents method used for steganographically hiding the data into image.
 * 
 * @author irelic
 *
 */
public interface SteganographyAlgorithm {

	/**
	 * Hides given data into image.
	 * 
	 * @param data
	 *            data to be hidden
	 * @param image
	 *            image used for hiding the data
	 * @throws InsufficientSpaceException
	 *             if data is too large for hiding into given image
	 */
	void hideData(byte[] data, BufferedImage image)
			throws InsufficientSpaceException;

	/**
	 * Extracts data from given stego image.
	 * 
	 * @param stegImage
	 *            stego image
	 * @return data hidden in given stego image
	 */
	byte[] extractData(BufferedImage stegImage);

	/**
	 * Returns maximum number of bytes this method can hide inside image.
	 * 
	 * @param image
	 * @return
	 */
	int getMaxBytes(BufferedImage image);
}
