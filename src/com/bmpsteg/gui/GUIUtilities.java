package com.bmpsteg.gui;

import java.awt.Component;

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
}
