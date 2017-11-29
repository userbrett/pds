/**
 * CryptoCipher.java
 * 
 * Personal Data Security (PDS)
 * Copyright 2017, PDS Software Solutions LLC - www.trustpds.com
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Brett Lee <crypto411@gmail.com>
 * @see <a href="https://www.trustpds.com/">www.trustpds.com</a>
 */

/*
 * This class provides one method: Creating a Cipher.
 */
package com.trustpds;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoCipher {

	/**
	 * Create a Cipher.
	 * 
	 * @param CryptoFile cryptoFile
	 * @param Gui gui
	 * @param String mode
	 * @return Cipher
	 * @throws CertificateException,
	 * @throws InvalidAlgorithmParameterException,
	 * @throws InvalidKeyException,
	 * @throws IOException,
	 * @throws KeyStoreException,
	 * @throws NoSuchAlgorithmException,
	 * @throws NoSuchPaddingException,
	 * @throws PDSException,
	 * @throws UnrecoverableKeyException
	 */
	protected Cipher getCipher (
			CryptoFile cryptoFile, 
			Gui gui,
			String mode
			
		) throws
			CertificateException,
			InvalidAlgorithmParameterException,
			InvalidKeyException,
			IOException,
			KeyStoreException,
			NoSuchAlgorithmException,
			NoSuchPaddingException,
			PDSException,
			UnrecoverableKeyException {

		byte[] ivBytes;
		String algorithmMode = "/CBC/PKCS5Padding";
		
		Key secretKey = new CryptoKey().getKey( cryptoFile );
		String algorithm = secretKey.getAlgorithm();
		Cipher cipher = Cipher.getInstance( algorithm + algorithmMode );
		if (StateObject.showcrypto) System.out.println("Using algorithm: " + algorithm + algorithmMode );

		if ( ! new Util().verifyAlgorithmAccess( algorithm, cryptoFile, gui ) ) {
			return null;
		}
		
		if ( cryptoFile.getIvBytes() == null ) {	// Existing files should have an IV.
			if ( mode.toLowerCase().equals("encrypt")) {
				// New file - Create a random IV.
				if (StateObject.showcrypto) System.out.println("Encrypting: Generating Initialization Vector." );
				ivBytes = new byte[ cipher.getBlockSize() ];
				SecureRandom random = new SecureRandom();
				random.nextBytes( ivBytes );
				cryptoFile.setIvBytes( ivBytes );
			} else if ( mode.toLowerCase().equals("decrypt")) { 
				// No IV - Cannot proceed.
				if (StateObject.showcrypto) System.out.println("Initialization Vector for Decryption not found." );
				return null;
			} else {
				// Application error
				if (StateObject.showcrypto) System.out.println("Invalid mode provided to getCipher: " + mode.toString());
				return null;
			}
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), algorithm );
		IvParameterSpec ivSpec = new IvParameterSpec( cryptoFile.getIvBytes() );
		if ( mode.toLowerCase().equals("encrypt")) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec );
		} else if ( mode.toLowerCase().equals(("decrypt"))) {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec );
		} else {
			if (StateObject.showcrypto) System.out.println("Bad cipher mode provided." );
			return null;
		}

		return cipher;
	}
}
