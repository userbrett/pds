/**
 * CryptoKey.java
 *
 * Personal Data Security (PDS)
 * Copyright 2009, Brett Lee - crypto411@gmail.com
 * Copyright 2016, PDS Software Solutions LLC - www.trustpds.com
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
 * This class provides methods to:
 * 1. Create a new Encryption Key.
 * 2. Read in an existing Key from a KeyStore.
 * 3. Check if a Key alias exists in a KeyStore.
 */

package com.trustpds;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class CryptoKey {

	private static final int SALT_BYTE_SIZE = 32;
	private static final int PBKDF2_ITERATIONS = 1000000;


	/**
	 * Create a new Key and save it in a KeyStore.
	 * 
	 * @param String keyStoreFileName
	 * @param char[] keyStorePass
	 * @param String keyAlgorithm
	 * @param Integer keyBits
	 * @param char[] keyPass
	 * @param String keyAlias
	 * @throws CertificateException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws PDSException
	 */
	void createKey (
			String keyStoreFileName,
			char[] keyStorePass,
			String keyAlgorithm,
			Integer keyBits,
			char[] keyPass,
			String keyAlias

		 ) throws
			CertificateException,
	 		InvalidKeySpecException,
			IOException,
	 		KeyStoreException,
			NoSuchAlgorithmException,
			PDSException {

		// Get a KeyStore.
		KeyStore keyStore = null;
		try {
			keyStore = CryptoKeyStore.getKeyStore( keyStoreFileName, keyStorePass );
		} catch (PDSException e) {
			throw e;
		}

		// Generate a random salt.
		SecureRandom random = new SecureRandom();
		byte salt[] = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);

		// Create the Key's specification.
		PBEKeySpec keySpec = new PBEKeySpec( keyPass, salt, PBKDF2_ITERATIONS, keyBits );

		// Get a Key factory.
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );

		// Generate a new Key.
		SecretKey key = new SecretKeySpec(keyFactory.generateSecret( keySpec ).getEncoded(), keyAlgorithm);

		// Add the new Key to the KeyStore.
		keyStore.setKeyEntry( keyAlias, key, keyPass, null);

		// Write the KeyStore with a new Key to persistent storage.
		CryptoKeyStore.saveKeyStore( keyStoreFileName, keyStore, keyStorePass );
		
		if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
		if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
		if (StateObject.showzero) System.out.println("Zeroing Credentials for Key." );
		if ( keyPass != null ) for ( int i=0; i < keyPass.length; i++ ) { keyPass[i] = '0'; }
		if (StateObject.showcrypto) System.out.println("Created Key: [Algorithm:" + keyAlgorithm + ", Bits:" + keyBits + "]");
	}



	/**
	 * Return a Key from a KeyStore.
	 * 
	 * @param CryptoFile cryptoFile
	 * @return Key
	 * @throws CertificateException	
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws PDSException
	 */
	Key getKey( CryptoFile cryptoFile )
			
		throws
			CertificateException,
			IOException,
			KeyStoreException,
			NoSuchAlgorithmException, 
			UnrecoverableKeyException,
			PDSException {

		Key keyFromStore = null;
		try {

			KeyStore keyStore = CryptoKeyStore.getKeyStore( cryptoFile.getKeyStoreName(), cryptoFile.getKeyStorePass() );
			if ( keyStore.containsAlias( cryptoFile.getKeyAlias() )) {  
				keyFromStore = keyStore.getKey( cryptoFile.getKeyAlias(), cryptoFile.getKeyPass() );
			}

		} catch (PDSException e) {
			throw e;
		}
		return keyFromStore;
	}



	/**
	 * Check if a Key alias exists in a given KeyStore.
	 * 
	 * @param String keyStoreFileName
	 * @param char[] keyStorePass
	 * @param String keyAlias
	 * @return boolean
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws PDSException
	 */
	boolean checkKeyExists (
			String keyStoreFileName,
			char[] keyStorePass,
			String keyAlias )

		throws
			CertificateException,
			KeyStoreException,
			IOException,
			NoSuchAlgorithmException,
			PDSException {

		KeyStore keyStore = null;
		try {
			keyStore = CryptoKeyStore.getKeyStore( keyStoreFileName, keyStorePass );
		} catch (PDSException e) {
			throw e;
		}
		
		if ( (keyStore != null) && (keyStore.containsAlias( keyAlias ))) {
			return true;
		} else {
			return false;
		}
	}
}	
