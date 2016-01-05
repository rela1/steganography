package com.bmpsteg.crypto;

/**
 * Represents symmetric password driven cryptography algorithm for protecting
 * the data.
 * 
 * @author irelic
 *
 */
public interface SymmetricCrypto {

	/**
	 * Encrypts data using given password as key.
	 * 
	 * @param data
	 *            data to be encrypted
	 * @param password
	 *            password used for generating key
	 * @return encrypted data or original data in case of exception
	 */
	byte[] encrypt(byte[] data, String password);

	/**
	 * Decrypts data using given password as key.
	 * 
	 * @param encryptedData
	 *            data to be decrypted
	 * @param password
	 *            password used for generating key
	 * @return decrypted data or original data in case of exception
	 */
	byte[] decrypt(byte[] encryptedData, String password);
}
