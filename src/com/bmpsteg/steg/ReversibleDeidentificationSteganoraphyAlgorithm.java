package com.bmpsteg.steg;

import java.awt.image.BufferedImage;

/**
 * Represents steganography algorithm used in reversible deidentification.
 * 
 * @author irelic
 *
 */
public interface ReversibleDeidentificationSteganoraphyAlgorithm extends
		SteganographyAlgorithm {

	/**
	 * Called before hiding the data into image to remove identification
	 * information from image.
	 * 
	 * @param originalImage
	 *            original image to remove identification information from
	 * @param xStart
	 *            start position x of identification information
	 * @param yStart
	 *            start position y of identification information
	 * @param xEnd
	 *            end position x of identification information
	 * @param yEnd
	 *            end position y of identification information
	 */
	void preprocessOriginalImage(BufferedImage originalImage, int xStart,
			int yStart, int xEnd, int yEnd);

}
