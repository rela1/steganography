package com.bmpsteg.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bmpsteg.steg.HidingReversibleDeidentificationSteganography;
import com.bmpsteg.steg.Utilities;

/**
 * Represents panel for hiding data image steganographically. It also offers
 * selecting part of image and hide it as well (for use in reversible
 * deidentification).
 * 
 * @author irelic
 *
 */
public class HideDataPanel extends AbstractStegoActionPanel {

	private static final long serialVersionUID = 1L;
	private JLabel maxDataLabel;
	private JLabel currentDataLabel;
	private JCheckBox useSelectedDataCheckbox;
	private JCheckBox hideSelectedDataCheckbox;
	private JComboBox<Integer> componentsToUseComboBox;
	private JComboBox<Integer> bitsPerComponentComboBox;
	private JButton loadDataButton;
	private JButton hideDataButton;
	private JPanel returnPanel;
	private JLabel selectedFileLabel;
	private File selectedFileToHide;

	/**
	 * Creates new panel for hiding data into image with given steganography
	 * algorithm and parent component in which this panel resides.
	 * 
	 * @param parentComponent
	 *            parent component in which this panel resides
	 * @param stegAlg
	 *            steganography algorithm used for hiding encrypted text
	 */
	public HideDataPanel(Component parentComponent,
			HidingReversibleDeidentificationSteganography stegAlg) {
		super(parentComponent, stegAlg);
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
		imageLabel.clearSelection();
		maxDataLabel.setText("Maximum bytes to hide: "
				+ stegAlg.getMaxBytes(image));
		currentDataLabel.setText("Current selection bytes: 0");
	}

	/**
	 * Initializes and layouts action panel.
	 */
	private void createActionPanel() {
		maxDataLabel = new JLabel("Maximum bytes to hide: 0");
		currentDataLabel = new JLabel("Current selection bytes: 0");
		useSelectedDataCheckbox = new JCheckBox("Use selection as data to hide");
		hideSelectedDataCheckbox = new JCheckBox("Hide selection");
		loadDataButton = new JButton("Load data to hide");
		hideDataButton = new JButton("Hide selected data");
		selectedFileLabel = new JLabel("Selected file to hide: ");
		returnPanel = new JPanel(new GridLayout(6, 1));
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
		JPanel useHideSelectionPanel = new JPanel(new FlowLayout());
		useHideSelectionPanel.add(useSelectedDataCheckbox);
		useHideSelectionPanel.add(hideSelectedDataCheckbox);
		JPanel usedBytesPanel = new JPanel(new FlowLayout());
		usedBytesPanel.add(maxDataLabel);
		usedBytesPanel.add(currentDataLabel);
		returnPanel.add(usedBytesPanel);
		returnPanel.add(useHideSelectionPanel);
		returnPanel.add(componentsToUsePanel);
		returnPanel.add(bitsPerComponentPanel);
		JPanel loadDataPanel = new JPanel(new FlowLayout());
		loadDataPanel.add(selectedFileLabel);
		loadDataPanel.add(loadDataButton);
		returnPanel.add(loadDataPanel);
		returnPanel.add(hideDataButton);
	}

	/**
	 * Sets size of current selection in selctable label.
	 */
	private void setCurrentSelectionBytes() {
		if (loadedImage == null || !useSelectedDataCheckbox.isSelected()
				|| !imageLabel.hasSelection()) {
			currentDataLabel.setText("Current selection bytes: 0");
			return;
		}
		int[] subImageCoordinates = getSelectionCoordinates();
		System.out.println(subImageCoordinates[0] + " "
				+ subImageCoordinates[1] + " " + subImageCoordinates[2] + " "
				+ subImageCoordinates[3]);
		int currentDataToHide = (subImageCoordinates[1]
				- subImageCoordinates[0] + 1)
				* (subImageCoordinates[3] - subImageCoordinates[2] + 1)
				* 3
				+ 54;
		currentDataLabel.setText("Current selection bytes: ~"
				+ currentDataToHide);
	}

	/**
	 * Calculates coordinates of selected subimage.
	 * 
	 * @return coordinates of selected subimage
	 */
	private int[] getSelectionCoordinates() {
		Point startPoint = imageLabel.getStartingPoint();
		Point endPoint = imageLabel.getEndingPoint();
		int startX = Math.min(startPoint.x, endPoint.x);
		int startY = Math.min(startPoint.y, endPoint.y);
		int endX = Math.max(startPoint.x, endPoint.x);
		int endY = Math.max(startPoint.y, endPoint.y);
		return new int[] { startX, endX, startY, endY };
	}

	/**
	 * Creates button actions of return panel.
	 */
	private void createActions() {
		imageLabel.addSelectableLabelListener(new SelectableLabelListener() {
			@Override
			public void newSelection(Point startPoint, Point endPoint) {
				setCurrentSelectionBytes();
			}
		});
		componentsToUseComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stegAlg.setComponentsToUse((int) componentsToUseComboBox
						.getSelectedItem());
				if (loadedImage != null) {
					maxDataLabel.setText("Maximum bytes to hide: "
							+ stegAlg.getMaxBytes(loadedImage));
				}
			}
		});
		bitsPerComponentComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stegAlg.setBitsPerComponent((int) bitsPerComponentComboBox
						.getSelectedItem());
				if (loadedImage != null) {
					maxDataLabel.setText("Maximum bytes to hide: "
							+ stegAlg.getMaxBytes(loadedImage));
				}
			}
		});
		useSelectedDataCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCurrentSelectionBytes();
			}
		});
		loadDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int response = jfc.showOpenDialog(parentComponent);
				if (response == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					if (selectedFile != null) {
						loadDataButton.setText(selectedFile.getAbsolutePath());
						selectedFileToHide = selectedFile;
					}
				}
			}
		});
		hideDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loadedImage == null) {
					GUIUtilities.showErrorDialog(parentComponent,
							"Nothing to hide!",
							"Please, load image for hiding the data first");
					return;
				}
				int[] subImageCoordinates = null;
				if ((useSelectedDataCheckbox.isSelected() || hideSelectedDataCheckbox
						.isSelected())) {
					if (!imageLabel.hasSelection()) {
						GUIUtilities
								.showErrorDialog(parentComponent,
										"No selection!",
										"Please select part of image you want to hide.");
						return;
					}
					subImageCoordinates = getSelectionCoordinates();
				}
				byte[] dataToHide = null;
				if (useSelectedDataCheckbox.isSelected()) {
					BufferedImage subImage = loadedImage
							.getSubimage(subImageCoordinates[0],
									subImageCoordinates[2],
									subImageCoordinates[1]
											- subImageCoordinates[0] + 1,
									subImageCoordinates[3]
											- subImageCoordinates[2] + 1);
					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(subImage, "bmp", baos);
						dataToHide = baos.toByteArray();
					} catch (Exception ex) {
						GUIUtilities.showErrorDialog(parentComponent,
								"Error extracting image selection!",
								ex.getMessage());
					}
				} else {
					try {
						dataToHide = Files.readAllBytes(selectedFileToHide
								.toPath());
					} catch (Exception ex) {
						ex.printStackTrace();
						GUIUtilities.showErrorDialog(parentComponent,
								"Error reading file to hide!", ex.getMessage());
					}
				}
				JFileChooser jfc = new JFileChooser();
				int response = jfc.showSaveDialog(parentComponent);
				if (response == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						BufferedImage loadedImageCopy = Utilities
								.copy(loadedImage);
						if (hideSelectedDataCheckbox.isSelected()) {
							stegAlg.preprocessOriginalImage(loadedImageCopy,
									subImageCoordinates[0],
									subImageCoordinates[2],
									subImageCoordinates[1],
									subImageCoordinates[3]);
						}
						stegAlg.hideData(dataToHide, loadedImageCopy);
						String filePath = selectedFile.getAbsolutePath();
						if (!filePath.endsWith(".bmp")) {
							filePath += ".bmp";
							selectedFile = new File(filePath);
						}
						ImageIO.write(loadedImageCopy, "bmp", selectedFile);
					} catch (Exception ex) {
						GUIUtilities.showErrorDialog(parentComponent,
								"Error hiding data!", ex.getMessage());
					}
				}
			}
		});
	}
}
