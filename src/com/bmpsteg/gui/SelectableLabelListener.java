package com.bmpsteg.gui;

import java.awt.Point;

/**
 * Represents listeners of observable selection panel. Observers are notified
 * each time new selection is available.
 * 
 * @author irelic
 *
 */
public interface SelectableLabelListener {

	void newSelection(Point startPoint, Point endPoint);
}
