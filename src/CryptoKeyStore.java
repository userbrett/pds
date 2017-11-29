/**
 * CryptoKeyStore.java
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
 * 1. Create a new KeyStore.
 * 2. Read in an existing KeyStore.
 * 3. Read in the elements within a KeyStore.
 * 4. Save changes to a KeyStore.
 */

package com.trustpds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;

class CryptoKeyStore {

	/**
	 * Create a new KeyStore and write it to persistent storage.
	 * 
	 * @param String keyStoreFileName
	 * @param String keyStoreType
	 * @param char[] keyStorePass
	 * @return boolean
	 * @throws CertificateException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException

	 */
	boolean createKeyStore (
			String keyStoreFileName,
			String keyStoreType,
			char[] keyStorePass

		 ) throws
			CertificateException,
			InvalidKeySpecException,
			IOException,
		 	KeyStoreException,
			NoSuchAlgorithmException {

		// Generate a new KeyStore.
		KeyStore keyStore = KeyStore.getInstance( keyStoreType );
		keyStore.load( null, keyStorePass);

		// Write the KeyStore to persistent storage.
		if ( saveKeyStore( keyStoreFileName, keyStore, keyStorePass ) ) {
			if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
			if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
			if (StateObject.showcrypto) System.out.println("Created KeyStore: [Type:" + keyStore.getType() + ", Provider:" + keyStore.getProvider() + "]");
			return true;
		} else {
			if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
			if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
			return false;
		}
	}



	/**
	 * Return a KeyStore object from persistent storage.
	 * 
	 * @param String keyStoreFileName
	 * @param char[] keyStorePass
	 * @return KeyStore
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws PDSException
	 */
	static KeyStore getKeyStore(
			String keyStoreFileName,
			char[] keyStorePass

		) throws
		 	CertificateException,
		 	FileNotFoundException,
		 	IOException,
		 	KeyStoreException, 
			NoSuchAlgorithmException,
			PDSException {

		// Get the the extension of KeyStore (JKS, JCEKS).
		int dotPos = keyStoreFileName.lastIndexOf(".");
		String extension = keyStoreFileName.substring(dotPos + 1);

		// Create an empty KeyStore in memory.
		final KeyStore keyStore = KeyStore.getInstance( extension );
		if (StateObject.showcrypto) System.out.println("KeyStore in use is type: " + keyStore.getType());

		// The remaining code loads the saved KeyStore to the in-memory KeyStore.
		File keyStoreFile = new File( keyStoreFileName );
		if ( keyStoreFile.exists() ) {

			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream( keyStoreFile );
				keyStore.load( inputStream, keyStorePass );
				
			} finally {
				try { if (inputStream != null) inputStream.close(); } catch( IOException e) {}
			}

		} else {
			if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
			if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
			throw new PDSException("Is the KeyStore accessible?");
		}

		if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
		if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
		return keyStore;
	}



	/**
	 * Return the elements within a KeyStore.
	 * 
	 * @param String keyStoreName
	 * @param char[] keyStorePass
	 * @return Object[][]
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws PDSException
	 */
	Object[][] getKeyStoreElements(
			String keyStoreName,
			char[] keyStorePass

		) throws
			CertificateException,
			KeyStoreException, 
			IOException, 
			NoSuchAlgorithmException, 
			PDSException {

		// Load the KeyStore.
		KeyStore keyStore;
		try {
			keyStore = getKeyStore( keyStoreName, keyStorePass );
		} catch (PDSException e) {
			if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
			if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
			throw e;
		}

		// Get Key Aliases and Creation Dates.
		int keyCount = keyStore.size();
		Object[][] keyStoreElements = new Object[keyCount][2];
		
		Enumeration<String> keyStoreEnum = keyStore.aliases();

		for (int count=0; keyStoreEnum.hasMoreElements(); ++count ) {

			String aliasName = keyStoreEnum.nextElement();
			keyStoreElements[count][0] = aliasName;
			keyStoreElements[count][1] = keyStore.getCreationDate(aliasName);

		}

		if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
		if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
		return keyStoreElements;
	}



	/**
	 * Write a KeyStore to persistent storage.
	 * 
	 * @param String keyStoreFileName
	 * @param KeyStore keyStore
	 * @param char[] keyStorePass
	 * @return boolean
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	static boolean saveKeyStore (
			String keyStoreFileName,
			final KeyStore 	keyStore,
			final char[] keyStorePass

		) throws
			CertificateException,
			IOException,
			KeyStoreException,
			NoSuchAlgorithmException {

		FileOutputStream outputStream = null;

		try {
			File keyStoreFile = new File( keyStoreFileName );
			outputStream = new FileOutputStream( keyStoreFile );
			keyStore.store( outputStream, keyStorePass );

		} finally {
			try { if (outputStream != null) outputStream.close(); } catch( IOException e ) {}
		}

		if (StateObject.showzero) System.out.println("Zeroing Credentials for KeyStore." );
		if ( keyStorePass != null ) for ( int i=0; i < keyStorePass.length; i++ ) { keyStorePass[i] = '0'; }
		return true;
	}
}
