package com.bmpsteg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.bmpsteg.crypto.AESCrypto;
import com.bmpsteg.steg.LSBSteganography;

/**
 * Creates paneled GUI application for steganographically hiding and extracting
 * the password encrypted data from image.
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
				JFrame frame = new JFrame("BMPSteg");
				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				JTabbedPane tabbedPane = new JTabbedPane();
				final LSBSteganography lsbSteg = new LSBSteganography();
				final AESCrypto aesCrypt = new AESCrypto();
				tabbedPane.addTab("Hide text", new HideTextPanel(frame,
						lsbSteg, aesCrypt));
				tabbedPane.addTab("Extract text", new ExtractTextPanel(frame,
						lsbSteg, aesCrypt));
				frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
				frame.setSize(WINDOW_DEFAULT_SIZE);
				frame.setVisible(true);
			}
		});
	}
}
