package com.bmpsteg.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

import com.bmpsteg.crypto.SymmetricCrypto;
import com.bmpsteg.steg.SteganographyAlgorithm;

/**
 * Represents panel for extracting the encrypted text from stego image.
 * 
 * @author irelic
 *
 */
public class ExtractTextPanel extends AbstractStegoActionPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates new panel for extracting the encrypted text from stego image with
	 * given steganography algorithm and parent component in which this panel
	 * resides.
	 * 
	 * @param parentComponent
	 *            parent component in which this panel resides
	 * @param stegAlg
	 *            steganography algorithm used for extracting encrypted text
	 * @param symmCrypt
	 *            symmetric cryptography algorithm used for decrypting the text
	 */
	public ExtractTextPanel(Component parentComponent,
			SteganographyAlgorithm stegAlg, SymmetricCrypto symmCrypt) {
		super(parentComponent, stegAlg, symmCrypt);
	}

	@Override
	protected String getActionButtonName() {
		return "Extract hidden text";
	}

	@Override
	protected ActionListener getActionButtonActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadedImage == null) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Nothing to extract!",
							"Please, load stego image first");
					return;
				}
				String password = new String(passwordField.getPassword());
				if (password.length() < 3) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Bad password!",
							"Password should be at least 3 characters long");
					return;
				}
				try {
					byte[] encryptedData = stegAlg.extractData(loadedImage);
					byte[] decryptedData = symmCrypt.decrypt(encryptedData,
							password);
					textArea.setText(new String(decryptedData,
							StandardCharsets.UTF_8));
				} catch (Exception ex) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Error hiding text", ex.getMessage());
				}
			}
		};
	}

	@Override
	protected boolean isTextAreaEditable() {
		return false;
	}

}
