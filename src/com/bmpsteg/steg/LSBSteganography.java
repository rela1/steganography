package com.bmpsteg.steg;

import java.awt.image.BufferedImage;

/**
 * Least significant bit stegranography method where least significant bit of
 * blue component in BMP image is used for hiding the data into image.
 * 
 * @author irelic
 *
 */
public class LSBSteganography implements SteganographyAlgorithm {

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
		int[] imageIndexes = new int[2];
		int imageWidth = image.getWidth();
		offset *= 8;
		for (int index = 0, length = data.length * 8; index < length; ++index) {
			calculateImageIndexesFromDataIndex(index + offset, imageWidth,
					imageIndexes);
			int imagePixel = image.getRGB(imageIndexes[0], imageIndexes[1]);
			int dataBitValue = Utilities.bitValue(data, index);
			if (dataBitValue == 1) {
				image.setRGB(imageIndexes[0], imageIndexes[1],
						Utilities.setLeastSignificantBit(imagePixel));
			} else {
				image.setRGB(imageIndexes[0], imageIndexes[1],
						Utilities.unsetLeastSignificantBit(imagePixel));
			}
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
		byte[] bytes = new byte[numberOfBytes];
		int[] imageIndexes = new int[2];
		int imageWidth = image.getWidth();
		offset *= 8;
		for (int index = 0, length = numberOfBytes * 8; index < length; ++index) {
			calculateImageIndexesFromDataIndex(index + offset, imageWidth,
					imageIndexes);
			int imagePixel = image.getRGB(imageIndexes[0], imageIndexes[1]);
			int imageBitValue = Utilities.getLeastSignificantBit(imagePixel);
			if (imageBitValue == 1) {
				Utilities.setBitValue(bytes, index);
			} else {
				Utilities.unsetBitValue(bytes, index);
			}
		}
		return bytes;
	}

	/**
	 * Calculates x and y coordinates for image from given index of data bit.
	 * 
	 * @param dataIndex
	 *            index of data bit
	 * @param imageWidth
	 *            image width
	 * @param indexes
	 *            array containing y and x coordinates (first is x coordinate,
	 *            then y coordinate)
	 */
	private void calculateImageIndexesFromDataIndex(int dataIndex,
			int imageWidth, int[] indexes) {
		indexes[0] = dataIndex % imageWidth;
		indexes[1] = dataIndex / imageWidth;
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
		return (int) Math.floor(image.getWidth() * image.getHeight() / 8.0)
				- Integer.BYTES;
	}
}
