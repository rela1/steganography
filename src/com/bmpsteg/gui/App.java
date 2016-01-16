package com.bmpsteg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.bmpsteg.steg.HidingReversibleDeidentificationSteganography;

/**
 * Creates paneled GUI application for steganographically deidentification or
 * hiding the data into image.
 * 
 * @author irelic
 *
 */
public class App {

	private static final Dimension WINDOW_DEFAULT_SIZE = new Dimension(1024,
			768);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Steganography deidentification");
				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				JTabbedPane tabbedPane = new JTabbedPane();
				final HidingReversibleDeidentificationSteganography lsbSteg = new HidingReversibleDeidentificationSteganography(
						1, 1);
				tabbedPane.addTab("Hide data",
						new HideDataPanel(frame, lsbSteg));
				tabbedPane.addTab("Extract data", new ExtractDataPanel(frame,
						lsbSteg));
				frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
				frame.setSize(WINDOW_DEFAULT_SIZE);
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
			}
		});
	}
}
