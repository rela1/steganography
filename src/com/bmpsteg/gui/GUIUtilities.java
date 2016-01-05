package com.bmpsteg.gui;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

/**
 * Contains utility methods used in GUI applications.
 * 
 * @author irelic
 *
 */
public class GUIUtilities {

	// cannot be instantiated
	private GUIUtilities() {
	}

	/**
	 * Shows error dialog with given title and message and OK option.
	 * 
	 * @param parentComponent
	 *            parent component to lock with dialog
	 * @param title
	 *            dialog title
	 * @param message
	 *            dialog message
	 */
	public static void showErrorDialog(Component parentComponent, String title,
			String message) {
		JOptionPane.showMessageDialog(parentComponent, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Returns copy of original buffered image in 4 BITS TYPE ABGR format.
	 * 
	 * @param original
	 *            original image
	 * @return copy of original buffered image in 4 BITS TYPE ABGR format
	 */
	public static BufferedImage copy(BufferedImage original) {
		int width = original.getWidth();
		int height = original.getHeight();
		BufferedImage copy = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				copy.setRGB(x, y, original.getRGB(x, y));
			}
		}
		return copy;
	}
}
