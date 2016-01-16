package com.bmpsteg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Represents selectable label where user can select part of it for later use.
 * 
 * @author irelic
 *
 */
public class SelectableLabel extends JComponent {

	private static final long serialVersionUID = 1L;
	private Point startingPoint;
	private Point endingPoint;
	private static final Color SELECTION_COLOR = new Color(0.5f, 0.5f, 0.5f,
			0.4f);
	private Set<SelectableLabelListener> listeners;
	private BufferedImage image;
	private static final Dimension DEFAULT_DIMENSIONS = new Dimension(400, 200);

	public SelectableLabel() {
		super();
		listeners = new HashSet<>();
		createSelectionListeners();
	}

	/**
	 * Creates selection mouse listeners.
	 */
	private void createSelectionListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					startingPoint = e.getPoint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (startingPoint != null) {
					endingPoint = e.getPoint();
				} else {
					startingPoint = null;
					endingPoint = null;
				}
				notifyListeners();
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					startingPoint = null;
					endingPoint = null;
					repaint();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (startingPoint != null) {
					endingPoint = e.getPoint();
					repaint();
				}
			}
		});
	}

	/**
	 * Returns information if label has part of it selected.
	 * 
	 * @return true if label has part of it selected, false otherwise
	 */
	public boolean hasSelection() {
		return startingPoint != null & endingPoint != null;
	}

	/**
	 * Returns starting point of label selection.
	 * 
	 * @return starting point of label selection
	 */
	public Point getStartingPoint() {
		return startingPoint;
	}

	/**
	 * Ending point of label selection.
	 * 
	 * @return ending point of label selection
	 */
	public Point getEndingPoint() {
		return endingPoint;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension size = getSize();
		if (image != null) {
			g.drawImage(image, 0, 0, Color.GRAY, null);
		} else {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, size.width, size.height);
		}
		if (startingPoint != null && endingPoint != null) {
			g.setColor(SELECTION_COLOR);
			int startingX = Math.min(startingPoint.x, endingPoint.x);
			int startingY = Math.min(startingPoint.y, endingPoint.y);
			int width = Math.abs(startingPoint.x - endingPoint.x);
			int height = Math.abs(startingPoint.y - endingPoint.y);
			g.fillRect(startingX, startingY, width, height);
			g.setColor(Color.BLACK);
			g.drawRect(startingX - 1, startingY - 1, width + 1, height + 1);
		}
	}

	/**
	 * Sets image that this component draws.
	 * 
	 * @param image
	 *            image that this component draws
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
		revalidate();
	}

	@Override
	public Dimension getPreferredSize() {
		if (image == null) {
			return DEFAULT_DIMENSIONS;
		}
		return new Dimension(image.getWidth(), image.getHeight());
	}

	/**
	 * Attaches given listener to this component.
	 * 
	 * @param listener
	 *            listener to attach to this component
	 */
	public void addSelectableLabelListener(SelectableLabelListener listener) {
		listeners = new HashSet<SelectableLabelListener>(listeners);
		listeners.add(listener);
	}

	/**
	 * Detaches given listener from this component.
	 * 
	 * @param listener
	 *            listener to detach from this component
	 */
	public void removeSelectableLabelListener(SelectableLabelListener listener) {
		listeners = new HashSet<SelectableLabelListener>(listeners);
		listeners.remove(listener);
	}

	/**
	 * Notifies observers of this component that new selection is available.
	 */
	private void notifyListeners() {
		for (SelectableLabelListener listener : listeners) {
			listener.newSelection(startingPoint, endingPoint);
		}
	}

	/**
	 * Clears selection of selectable label.
	 */
	public void clearSelection() {
		startingPoint = null;
		endingPoint = null;
	}

}
