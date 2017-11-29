Personal Data Security (PDS)

Copyright 2009, Brett Lee - crypto411@gmail.com

Copyright 2016, PDS Software Solutions LLC - www.trustpds.com


The Java class files provided here are part of the Personal Data
Security software application.  These classes have been released
to demonstrate how PDS has implemented the the cryptographic
components within the application, and to seek comments on any
and all improvements that could be made to enhance the security
of the cryptography within PDS.  The classes are released under
the MIT license.


The cryptographic functionalities within PDS include:

  1. Creation of cipher Keys - AES, TDEA & DES.

  2. Storage and retrieval of cipher Keys.

  3. Creation of encryption/decryption Ciphers.

  4. Creation of Initialization Vectors.


The classes and their cryptographic code are as follows:

  1. CryptoKeyStore.java - Creating, reading and saving KeyStores.

  2. CryptoKey.java - Creating, storing and retrieving Keys.

  3. CryptoCipher.java - Creating ciphers for encrypting/decrypting.

