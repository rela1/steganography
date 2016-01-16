package com.bmpsteg.steg;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Contains utility methods.
 * 
 * @author irelic
 *
 */
public class Utilities {

	// cannot be instantiated
	private Utilities() {
	}

	private static final int BIT_VALUE_MASK = 1;
	private static final int[] BYTE_BIT_MASKS_SET = new int[] { 128, 64, 32,
			16, 8, 4, 2, 1 };
	private static final int[] BYTE_BIT_MASKS_CLEAR = new int[] {
			~BYTE_BIT_MASKS_SET[0], ~BYTE_BIT_MASKS_SET[1],
			~BYTE_BIT_MASKS_SET[2], ~BYTE_BIT_MASKS_SET[3],
			~BYTE_BIT_MASKS_SET[4], ~BYTE_BIT_MASKS_SET[5],
			~BYTE_BIT_MASKS_SET[6], ~BYTE_BIT_MASKS_SET[7] };
	private static final int[] BIT_SHIFTS = new int[] { 7, 6, 5, 4, 3, 2, 1, 0 };
	private static final int[] RGB_COMPONENT_SHIFTS = { 0, 8, 16 };
	private static final int[] RGB_BIT_SHIFTS = new int[] { 0, 1, 2, 3, 4, 5,
			6, 7 };
	private static final int[] RGB_BIT_MASKS_SET = new int[] { 1, 2, 4, 8, 16,
			32, 64, 128 };

	/**
	 * Returns int from array of bytes representing int value.
	 * 
	 * @param bytes
	 *            bytes representing long value
	 * @return int value from bytes
	 */
	public static int fromBytes(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	/**
	 * Returns byte array representing given int value.
	 * 
	 * @param num
	 *            int value
	 * @return byte array representing given int value
	 */
	public static byte[] fromInt(int num) {
		return ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(num)
				.array();
	}

	/**
	 * Returns bit value of given data byte array for given bit index.
	 * 
	 * @param data
	 *            data byte array
	 * @param index
	 *            bit index
	 * @return bit value on given index; either 0 or 1
	 */
	public static byte bitValue(byte[] data, int index) {
		int wordIndex = index / Byte.SIZE;
		int inWordIndex = index % Byte.SIZE;
		return (byte) ((data[wordIndex] >> BIT_SHIFTS[inWordIndex]) & BIT_VALUE_MASK);
	}

	/**
	 * Sets bit of given data byte array on given bit index.
	 * 
	 * @param data
	 *            data byte array
	 * @param index
	 *            bit index
	 */
	public static void setBitValue(byte[] data, int index) {
		int wordIndex = index / Byte.SIZE;
		int inWordIndex = index % Byte.SIZE;
		data[wordIndex] = (byte) (data[wordIndex] | BYTE_BIT_MASKS_SET[inWordIndex]);
	}

	/**
	 * Unsets bit of given data byte array on given bit index.
	 * 
	 * @param data
	 *            data byte array
	 * @param index
	 *            bit index
	 */
	public static void unsetBitValue(byte[] data, int index) {
		int wordIndex = index / Byte.SIZE;
		int inWordIndex = index % Byte.SIZE;
		data[wordIndex] = (byte) (data[wordIndex] & BYTE_BIT_MASKS_CLEAR[inWordIndex]);
	}

	/**
	 * Returns bit value of integer representing RGB color for given component
	 * index and bit index.
	 * 
	 * @param rgbInt
	 *            integer representing RGB color
	 * @param componentIndex
	 *            color component index; in range 0-2 (0 for B component, 1 for
	 *            G component, 2 for R component)
	 * @param bitIndex
	 *            bit index; in range 0-7 for bit value in given component
	 * @return 1 or 0
	 */
	public static byte getRGBIntBitValue(int rgbInt, int componentIndex,
			int bitIndex) {
		return (byte) (((rgbInt >> RGB_COMPONENT_SHIFTS[componentIndex]) >> RGB_BIT_SHIFTS[bitIndex]) & BIT_VALUE_MASK);
	}

	/**
	 * Returns integer representing RGB color with bit on given component index
	 * and bit index set.
	 * 
	 * @param rgbInt
	 *            integer representing RGB color
	 * @param componentIndex
	 *            color component index; in range 0-2 (0 for B component, 1 for
	 *            G component, 2 for R component)
	 * @param bitIndex
	 *            bit index; in range 0-7 for bit value in given component
	 * @return integer representing RGB color with bit on given component index
	 *         and bit index set
	 */
	public static int setRGBIntBitValue(int rgbInt, int componentIndex,
			int bitIndex) {
		return rgbInt
				| (RGB_BIT_MASKS_SET[bitIndex] << RGB_COMPONENT_SHIFTS[componentIndex]);
	}

	/**
	 * Returns integer representing RGB color with bit on given component index
	 * and bit index cleared.
	 * 
	 * @param rgbInt
	 *            integer representing RGB color
	 * @param componentIndex
	 *            color component index; in range 0-2 (0 for B component, 1 for
	 *            G component, 2 for R component)
	 * @param bitIndex
	 *            bit index; in range 0-7 for bit value in given component
	 * @return integer representing RGB color with bit on given component index
	 *         and bit index cleared
	 */
	public static int unsetRGBIntBitValue(int rgbInt, int componentIndex,
			int bitIndex) {
		return rgbInt
				& ~(RGB_BIT_MASKS_SET[bitIndex] << RGB_COMPONENT_SHIFTS[componentIndex]);
	}

	/**
	 * Returns copy of original buffered image in INT TYPE RBG format.
	 * 
	 * @param original
	 *            original image
	 * @return copy of original buffered image in INT TYPE RBG format
	 */
	public static BufferedImage copy(BufferedImage original) {
		int width = original.getWidth();
		int height = original.getHeight();
		BufferedImage copy = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				copy.setRGB(x, y, original.getRGB(x, y));
			}
		}
		return copy;
	}
}
