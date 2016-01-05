package com.bmpsteg.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Represents AES-128bit encrypting/decrypting algorithm using SHA-1 for
 * generating key from password.
 * 
 * @author irelic
 *
 */
public class AESCrypto implements SymmetricCrypto {

	@Override
	public byte[] encrypt(byte[] data, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] keyDigest = md.digest();
			keyDigest = Arrays.copyOf(keyDigest, 16);
			Cipher c = Cipher.getInstance("AES");
			SecretKeySpec k = new SecretKeySpec(keyDigest, "AES");
			c.init(Cipher.ENCRYPT_MODE, k);
			byte[] encryptedData = c.doFinal(data);
			return encryptedData;
		} catch (Exception ignorable) {
			return data;
		}
	}

	@Override
	public byte[] decrypt(byte[] encryptedData, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] keyDigest = md.digest();
			keyDigest = Arrays.copyOf(keyDigest, 16);
			Cipher c = Cipher.getInstance("AES");
			SecretKeySpec k = new SecretKeySpec(keyDigest, "AES");
			c.init(Cipher.DECRYPT_MODE, k);
			byte[] data = c.doFinal(encryptedData);
			return data;
		} catch (Exception ignorable) {
			return encryptedData;
		}
	}

}
