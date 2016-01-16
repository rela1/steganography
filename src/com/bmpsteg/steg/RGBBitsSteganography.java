package com.bmpsteg.steg;

import java.awt.image.BufferedImage;

/**
 * Stegranography method where n bits of k components in BMP image is used for
 * hiding the data into image.
 * 
 * @author irelic
 *
 */
public class RGBBitsSteganography implements SteganographyAlgorithm {

	private int componentsToUse;
	private int bitsPerComponent;

	/**
	 * Creates new steganography algorithm with given number of color components
	 * to use in given order (blue component is used first, green component is
	 * used second and red component is used third) and given number of bits per
	 * component to use. Next significant bit is used only after all component's
	 * current order bits are used.
	 * 
	 * @param componentsToUse
	 *            number of color components to use (between 1 and 3, inclusive)
	 * @param bitsPerComponent
	 *            number of bits per component to use (between 1 and 8,
	 *            inclusive)
	 */
	public RGBBitsSteganography(int componentsToUse, int bitsPerComponent) {
		checkComponentsToUse(componentsToUse);
		checkBitsPerComponent(bitsPerComponent);
		this.componentsToUse = componentsToUse;
		this.bitsPerComponent = bitsPerComponent;
	}

	/**
	 * Checks if number of bits per component is between 1 and 8, inclusive.
	 * 
	 * @param bitsPerComponent
	 *            number of bits per component
	 */
	private void checkBitsPerComponent(int bitsPerComponent) {
		if (bitsPerComponent < 1 || bitsPerComponent > 8) {
			throw new IllegalArgumentException(
					"Number of bits per component must be between 1 and 8, inclusive!");
		}
	}

	/**
	 * Checks if number of components is between 1 and 3, inclusive.
	 * 
	 * @param componentsToUse
	 *            number of color components to use
	 */
	private void checkComponentsToUse(int componentsToUse) {
		if (componentsToUse < 1 || componentsToUse > 3) {
			throw new IllegalArgumentException(
					"Number of color components to use must be between 1 and 3, inclusive!");
		}
	}

	@Override
	public void hideData(byte[] data, BufferedImage image)
			throws InsufficientSpaceException {
		int numberOfBytes = data.length;
		if (numberOfBytes > getMaxBytes(image)) {
			throw new InsufficientSpaceException(String.format(
					"Maximum data size is %d bytes! Given size: %d bytes",
					getMaxBytes(image), numberOfBytes));
		}
		byte[] dataLength = Utilities.fromInt(numberOfBytes);
		writeDataToImage(image, dataLength, 0);
		writeDataToImage(image, data, dataLength.length);
	}

	/**
	 * Writes given data to image using LSB.
	 * 
	 * @param image
	 *            image for LSB stego
	 * @param data
	 *            data to be written
	 * @param offset
	 *            byte offset for starting writing
	 */
	private void writeDataToImage(BufferedImage image, byte[] data, int offset) {
		int[] indexes = new int[4];
		int imageWidth = image.getWidth();
		offset *= 8;
		int imagePixels = imageWidth * image.getHeight();
		int numberOfDataBits = data.length * 8;
		for (int dataBitIndex = 0; dataBitIndex < numberOfDataBits; ++dataBitIndex) {
			calculateImageIndexesFromDataIndex(dataBitIndex, offset,
					imageWidth, imagePixels, indexes);
			int imagePixel = image.getRGB(indexes[0], indexes[1]);
			int dataBitValue = Utilities.bitValue(data, dataBitIndex);
			if (dataBitValue == 1) {
				imagePixel = Utilities.setRGBIntBitValue(imagePixel,
						indexes[2], indexes[3]);
			} else {
				imagePixel = Utilities.unsetRGBIntBitValue(imagePixel,
						indexes[2], indexes[3]);
			}
			image.setRGB(indexes[0], indexes[1], imagePixel);
		}
	}

	/**
	 * Loads data from stego image.
	 * 
	 * @param image
	 *            stego image
	 * @param numberOfBits
	 *            number of bits to load
	 * @param offset
	 *            byte offset for starting loading
	 * @return data from stego image
	 */
	private byte[] loadDataFromImage(BufferedImage image, int numberOfBytes,
			int offset) {
		byte[] data = new byte[numberOfBytes];
		int[] indexes = new int[4];
		int imageWidth = image.getWidth();
		offset *= 8;
		int imagePixels = imageWidth * image.getHeight();
		int numberOfDataBits = data.length * 8;
		for (int dataBitIndex = 0; dataBitIndex < numberOfDataBits; ++dataBitIndex) {
			calculateImageIndexesFromDataIndex(dataBitIndex, offset,
					imageWidth, imagePixels, indexes);
			int imagePixel = image.getRGB(indexes[0], indexes[1]);
			int imageBitValue = Utilities.getRGBIntBitValue(imagePixel,
					indexes[2], indexes[3]);
			if (imageBitValue == 1) {
				Utilities.setBitValue(data, dataBitIndex);
			} else {
				Utilities.unsetBitValue(data, dataBitIndex);
			}
		}
		return data;
	}

	/**
	 * Calculates x and y coordinates, component index and bit index for image
	 * from given index of data bit.
	 * 
	 * @param dataIndex
	 *            index of data bit
	 * @param offset
	 *            offset of data
	 * @param imageWidth
	 *            image width
	 * @param imagePixels
	 *            number of pixels in image
	 * @param indexes
	 *            array containing y and x coordinates (first is x coordinate,
	 *            then y coordinate)
	 */
	private void calculateImageIndexesFromDataIndex(int dataIndex, int offset,
			int imageWidth, int imagePixels, int[] indexes) {
		dataIndex += offset;
		int imagePass = dataIndex / imagePixels;
		indexes[2] = imagePass % componentsToUse;
		indexes[3] = imagePass / componentsToUse;
		int imagePixel = dataIndex - (imagePixels * (imagePass));
		indexes[0] = imagePixel % imageWidth;
		indexes[1] = imagePixel / imageWidth;
	}

	@Override
	public byte[] extractData(BufferedImage stegImage) {
		int dataLength = Utilities.fromBytes(loadDataFromImage(stegImage,
				Integer.SIZE / Byte.SIZE, 0));
		return loadDataFromImage(stegImage, dataLength, Integer.SIZE
				/ Byte.SIZE);
	}

	@Override
	public int getMaxBytes(BufferedImage image) {
		int imageArea = image.getWidth() * image.getHeight();
		int totalNumberOfBits = imageArea * componentsToUse * bitsPerComponent;
		return Math.max((int) Math.floor(totalNumberOfBits / 8.0)
				- Integer.BYTES, 0);
	}

	/**
	 * Sets number of components to use per pixel for data hiding.
	 * 
	 * @param componentsToUse
	 *            components to use per pixel for data hiding
	 */
	public void setComponentsToUse(int componentsToUse) {
		checkComponentsToUse(componentsToUse);
		this.componentsToUse = componentsToUse;
	}

	/**
	 * Sets number of bits per component to use for data hiding.
	 * 
	 * @param bitsPerComponent
	 *            number of bits per component to use for data hiding
	 */
	public void setBitsPerComponent(int bitsPerComponent) {
		checkBitsPerComponent(bitsPerComponent);
		this.bitsPerComponent = bitsPerComponent;
	}
}
