package com.bmpsteg.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bmpsteg.steg.HidingReversibleDeidentificationSteganography;

/**
 * Represents abstract panel for steganography action (hiding or extracting the
 * data).
 * 
 * @author irelic
 *
 */
public abstract class AbstractStegoActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected final HidingReversibleDeidentificationSteganography stegAlg;
	protected SelectableLabel imageLabel;
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
	 *            steganography algorithm used for hiding encrypted text and
	 *            deidentification
	 * @param symmCrypt
	 *            symmetric cryptography algorithm used for encrypting the text
	 */
	public AbstractStegoActionPanel(Component parentComponent,
			HidingReversibleDeidentificationSteganography stegAlg) {
		this.stegAlg = stegAlg;
		this.parentComponent = parentComponent;
		createGUI();
		createActions();
	}

	/**
	 * Returns panel containing graphics with action listeners attached to
	 * perform work other than image loading.
	 * 
	 * @return panel containing graphics with action listeners attached to
	 *         perform work other than image loading
	 */
	protected abstract JPanel getActionPanel();

	/**
	 * Called when new image is loaded into selectable label.
	 * 
	 * @param image
	 *            new image that is loaded into selectable label
	 */
	protected abstract void newImageLoaded(BufferedImage image);

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
							newImageLoaded(loadedImage);
							imageLabel.setImage(loadedImage);
						} catch (Exception ex) {
							GUIUtilities.showErrorDialog(parentComponent,
									"Error loading image!", ex.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * Initializes and layouts GUI.
	 */
	private void createGUI() {
		loadImageButton = new JButton("Load image...");
		imageLabel = new SelectableLabel();
		setLayout(new BorderLayout());
		JPanel imageLoadPanel = new JPanel(new BorderLayout());
		imageLoadPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
		imageLoadPanel.add(loadImageButton, BorderLayout.SOUTH);
		imageLoadPanel.setBorder(BorderFactory.createTitledBorder("Image"));
		add(imageLoadPanel, BorderLayout.CENTER);
		add(getActionPanel(), BorderLayout.SOUTH);
	}

}
