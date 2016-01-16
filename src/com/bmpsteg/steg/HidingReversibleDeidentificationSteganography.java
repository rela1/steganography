package com.bmpsteg.steg;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HidingReversibleDeidentificationSteganography extends
		RGBBitsSteganography implements
		ReversibleDeidentificationSteganoraphyAlgorithm {

	private static final int ALL_BLACK_EVERYTHING = new Color(0, 0, 0).getRGB();

	/**
	 * Creates new steganography algorithm with given number of color components
	 * to use in given order (blue component is used first, green component is
	 * used second and red component is used third) and given number of bits per
	 * component to use. Next significant bit is used only after all component's
	 * current order bits are used.
	 * 
	 * @param componentsToUse
	 *            number of color components to use (between 1 and 3, inclusive)
	 * @param bitsPerComponent
	 *            number of bits per component to use (between 1 and 8,
	 *            inclusive)
	 */
	public HidingReversibleDeidentificationSteganography(int componentsToUse,
			int bitsPerComponent) {
		super(componentsToUse, bitsPerComponent);
	}

	@Override
	public void preprocessOriginalImage(BufferedImage originalImage,
			int xStart, int yStart, int xEnd, int yEnd) {
		for (int x = xStart; x <= xEnd; ++x) {
			for (int y = yStart; y <= yEnd; ++y) {
				originalImage.setRGB(x, y, ALL_BLACK_EVERYTHING);
			}
		}
	}
}
