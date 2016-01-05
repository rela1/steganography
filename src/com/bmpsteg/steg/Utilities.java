package com.bmpsteg.steg;

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

	private static final int BIT_VALUE_SHIFT = Byte.SIZE - 1;
	private static final int BIT_VALUE_MASK = 1;
	private static final int UNSET_LEAST_MASK = ~1;
	private static final int SET_LEAST_MASK = 1;
	private static final int[] BYTE_BIT_MASKS_SET = new int[] { 128, 64, 32,
			16, 8, 4, 2, 1 };
	private static final int[] BYTE_BIT_MASKS_CLEAR = new int[] {
			~BYTE_BIT_MASKS_SET[0], ~BYTE_BIT_MASKS_SET[1],
			~BYTE_BIT_MASKS_SET[2], ~BYTE_BIT_MASKS_SET[3],
			~BYTE_BIT_MASKS_SET[4], ~BYTE_BIT_MASKS_SET[5],
			~BYTE_BIT_MASKS_SET[6], ~BYTE_BIT_MASKS_SET[7] };

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
	public static int bitValue(byte[] data, int index) {
		int wordIndex = index / Byte.SIZE;
		int inWordIndex = index % Byte.SIZE;
		return (data[wordIndex] >> (BIT_VALUE_SHIFT - inWordIndex))
				& BIT_VALUE_MASK;
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
	 * Unsets least significant bit of given number.
	 * 
	 * @param num
	 *            number
	 * @return number with unsetted least significant bit
	 */
	public static int unsetLeastSignificantBit(int num) {
		return num & UNSET_LEAST_MASK;
	}

	/**
	 * Sets least significant bit of given number.
	 * 
	 * @param num
	 *            number
	 * @return number with setted least significant bit
	 */
	public static int setLeastSignificantBit(int num) {
		return num | SET_LEAST_MASK;
	}

	/**
	 * Returns bit value of least significant bit of given number.
	 * 
	 * @param num
	 *            number
	 * @return bit value on least significant bit; either 0 or 1
	 */
	public static int getLeastSignificantBit(int num) {
		return num & BIT_VALUE_MASK;
	}
}
