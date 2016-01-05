package com.bmpsteg.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import com.bmpsteg.crypto.SymmetricCrypto;
import com.bmpsteg.steg.SteganographyAlgorithm;

/**
 * Represents panel for hiding the encrypted text into image steganographically.
 * 
 * @author irelic
 *
 */
public class HideTextPanel extends AbstractStegoActionPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates new panel for hiding encrypted text into image with given
	 * steganography algorithm and parent component in which this panel resides.
	 * 
	 * @param parentComponent
	 *            parent component in which this panel resides
	 * @param stegAlg
	 *            steganography algorithm used for hiding encrypted text
	 * @param symmCrypt
	 *            symmetric cryptography algorithm used for encrypting the text
	 */
	public HideTextPanel(Component parentComponent,
			SteganographyAlgorithm stegAlg, SymmetricCrypto symmCrypt) {
		super(parentComponent, stegAlg, symmCrypt);
	}

	@Override
	protected String getActionButtonName() {
		return "Save image with text hidden";
	}

	@Override
	protected ActionListener getActionButtonActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadedImage == null) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Nothing to save!",
							"Please, load image for hiding the text first");
					return;
				}
				String password = new String(passwordField.getPassword());
				if (password.length() < 3) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Bad password!",
							"Password should be at least 3 characters long");
					return;
				}
				JFileChooser jfc = new JFileChooser();
				int response = jfc.showSaveDialog(parentComponent);
				if (response == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						String text = textArea.getText();
						byte[] encryptedData = symmCrypt
								.encrypt(text.getBytes(StandardCharsets.UTF_8),
										password);
						BufferedImage loadedImageCopy = GUIUtilities
								.copy(loadedImage);
						stegAlg.hideData(encryptedData, loadedImageCopy);
						String filePath = selectedFile.getAbsolutePath();
						if (!filePath.endsWith(".bmp")) {
							filePath += ".bmp";
							selectedFile = new File(filePath);
						}
						ImageIO.write(loadedImageCopy, "bmp", selectedFile);
					} catch (Exception ex) {
						GUIUtilities.showErrorDialog(parentComponent,
								"Error hiding text", ex.getMessage());
					}
				}
			}
		};
	}

	@Override
	protected boolean isTextAreaEditable() {
		return true;
	}
}
