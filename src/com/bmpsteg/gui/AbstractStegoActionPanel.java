package com.bmpsteg.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bmpsteg.crypto.SymmetricCrypto;
import com.bmpsteg.steg.SteganographyAlgorithm;

/**
 * Represents abstract panel for steganography action (hiding or extracting the
 * data).
 * 
 * @author irelic
 *
 */
public abstract class AbstractStegoActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected final SteganographyAlgorithm stegAlg;
	protected final SymmetricCrypto symmCrypt;
	protected JLabel imageLabel;
	protected BufferedImage loadedImage;
	protected JButton loadImageButton;
	protected JButton actionButton;
	protected JTextArea textArea;
	protected JPasswordField passwordField;
	protected Component parentComponent;

	/**
	 * Creates new abstract panel for steganography action (hiding or extracting
	 * the data) with given steganography algorithm, symmetric cryptography
	 * algorithm and parent component in which this panel resides.
	 * 
	 * @param parentComponent
	 *            parent component in which this panel resides
	 * @param stegAlg
	 *            steganography algorithm used for hiding encrypted text
	 * @param symmCrypt
	 *            symmetric cryptography algorithm used for encrypting the text
	 */
	public AbstractStegoActionPanel(Component parentComponent,
			SteganographyAlgorithm stegAlg, SymmetricCrypto symmCrypt) {
		this.stegAlg = stegAlg;
		this.symmCrypt = symmCrypt;
		this.parentComponent = parentComponent;
		createGUI();
		createActions();
	}

	/**
	 * Returns name of action button.
	 * 
	 * @return name of action button
	 */
	protected abstract String getActionButtonName();

	/**
	 * Returns action button action listener.
	 * 
	 * @return action button action listener
	 */
	protected abstract ActionListener getActionButtonActionListener();

	/**
	 * Returns whether text area is editable or not
	 * 
	 * @return true if text area is editable, false otherwise
	 */
	protected abstract boolean isTextAreaEditable();

	/**
	 * Creates button actions for loading image and steganography action.
	 */
	private void createActions() {
		loadImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(new FileNameExtensionFilter("Image files",
						"jpg", "jpeg", "png", "gif", "bmp", "wbmp"));
				int response = jfc.showOpenDialog(parentComponent);
				if (response == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					if (selectedFile != null) {
						try {
							loadedImage = ImageIO.read(selectedFile);
							imageLabel.setIcon(new ImageIcon(loadedImage));
						} catch (Exception ex) {
							GUIUtilities.showErrorDialog(parentComponent,
									"Error loading image!", ex.getMessage());
						}
					}
				}
			}
		});
		actionButton.addActionListener(getActionButtonActionListener());

	}

	/**
	 * Initializes and layouts GUI.
	 */
	private void createGUI() {
		loadImageButton = new JButton("Load image...");
		actionButton = new JButton(getActionButtonName());
		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		textArea = new JTextArea(10, 20);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(isTextAreaEditable());
		passwordField = new JPasswordField(20);
		setLayout(new BorderLayout());
		JPanel imageLoadPanel = new JPanel(new BorderLayout());
		imageLoadPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
		imageLoadPanel.add(loadImageButton, BorderLayout.SOUTH);
		imageLoadPanel.setBorder(BorderFactory.createTitledBorder("Image"));
		add(imageLoadPanel, BorderLayout.CENTER);
		JPanel textPasswordHidePanel = new JPanel(new BorderLayout());
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		textPanel.setBorder(BorderFactory.createTitledBorder("Text"));
		JPanel passwordPanel = new JPanel(new BorderLayout());
		passwordPanel.add(passwordField, BorderLayout.CENTER);
		passwordPanel.setBorder(BorderFactory.createTitledBorder("Password"));
		textPasswordHidePanel.add(textPanel, BorderLayout.CENTER);
		JPanel passwordSavePanel = new JPanel(new FlowLayout());
		passwordSavePanel.add(passwordPanel);
		passwordSavePanel.add(actionButton);
		textPasswordHidePanel.add(passwordSavePanel, BorderLayout.SOUTH);
		add(textPasswordHidePanel, BorderLayout.EAST);
	}

}
