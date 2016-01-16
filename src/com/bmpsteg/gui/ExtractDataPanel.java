package com.bmpsteg.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bmpsteg.steg.HidingReversibleDeidentificationSteganography;

/**
 * Represents panel for extracting the data from stego image.
 * 
 * @author irelic
 *
 */
public class ExtractDataPanel extends AbstractStegoActionPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox<Integer> componentsToUseComboBox;
	private JComboBox<Integer> bitsPerComponentComboBox;
	private JButton extractDataButton;
	private JPanel returnPanel;

	/**
	 * Creates new panel for extracting data from stego image with given
	 * steganography algorithm and parent component in which this panel resides.
	 * 
	 * @param parentComponent
	 *            parent component in which this panel resides
	 * @param stegAlg
	 *            steganography algorithm used for extracting encrypted text
	 */
	public ExtractDataPanel(Component parentComponent,
			HidingReversibleDeidentificationSteganography stegAlg) {
		super(parentComponent, stegAlg);
	}

	/**
	 * Initializes and layouts action panel.
	 */
	private void createActionPanel() {
		returnPanel = new JPanel(new GridLayout(3, 1));
		extractDataButton = new JButton("Extract hidden data");
		JPanel componentsToUsePanel = new JPanel(new FlowLayout());
		componentsToUseComboBox = new JComboBox<Integer>(new Integer[] { 1, 2,
				3 });
		componentsToUsePanel.add(new JLabel(
				"Color components to use per pixel: "));
		componentsToUsePanel.add(componentsToUseComboBox);
		JPanel bitsPerComponentPanel = new JPanel(new FlowLayout());
		bitsPerComponentComboBox = new JComboBox<Integer>(new Integer[] { 1, 2,
				3, 4, 5, 6, 7, 8 });
		bitsPerComponentPanel.add(new JLabel(
				"Bits to use per color component: "));
		bitsPerComponentPanel.add(bitsPerComponentComboBox);
		returnPanel.add(componentsToUsePanel);
		returnPanel.add(bitsPerComponentPanel);
		returnPanel.add(extractDataButton);
	}

	/**
	 * Creates button actions of return panel.
	 */
	private void createActions() {
		componentsToUseComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stegAlg.setComponentsToUse((int) componentsToUseComboBox
						.getSelectedItem());
			}
		});
		bitsPerComponentComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stegAlg.setBitsPerComponent((int) bitsPerComponentComboBox
						.getSelectedItem());
			}
		});
		extractDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadedImage == null) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Nothing to extract!",
							"Please, load stego image first");
					return;
				}
				try {
					JFileChooser jfc = new JFileChooser();
					int response = jfc.showSaveDialog(parentComponent);
					if (response == JFileChooser.APPROVE_OPTION) {
						File selectedFile = jfc.getSelectedFile();
						byte[] extractedData = stegAlg.extractData(loadedImage);
						Files.write(selectedFile.toPath(), extractedData);
					}
				} catch (Exception ex) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Error extracting data!", ex.getMessage());
				}
			}
		});
	}

	@Override
	protected JPanel getActionPanel() {
		if (returnPanel == null) {
			createActionPanel();
			createActions();
		}
		return returnPanel;
	}

	@Override
	protected void newImageLoaded(BufferedImage image) {
	}

}
