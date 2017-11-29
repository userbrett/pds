# README.KeyStoreBrute.txt
# 
# KeyStoreBrute may be obtained from the author at:
# https://github.com/bes/KeystoreBrute
#
# ##


Once compiled, these classes will perform a brute force
test against Keys in a KeyStore.  It does so by attempting
a "brute force" attack to guess the password of a Key.

These steps demonstrate how to run a sample execution:

$ ls -l
total 24
-rw-rw-r-- 1 brett brett 8397 Apr 13 14:55 Breaker.java
-rw-rw-r-- 1 brett brett 1910 Apr 13 14:56 BruteMain.java
-rw-rw-r-- 1 brett brett 2044 Apr 13 14:52 test.JCEKS

    Note:  test.JCEKS is a KeyStore with a Key in it.

$ javac B*.java

$ ls -l
total 48
-rw-rw-r-- 1 brett brett  172 Sep 17 07:43 Breaker$1.class
-rw-rw-r-- 1 brett brett 1776 Sep 17 07:43 Breaker$PasswordGenerator.class
-rw-rw-r-- 1 brett brett 1659 Sep 17 07:43 Breaker$PasswordTester.class
-rw-rw-r-- 1 brett brett  603 Sep 17 07:43 Breaker$Shutdown.class
-rw-rw-r-- 1 brett brett 3722 Sep 17 07:43 Breaker.class
-rw-rw-r-- 1 brett brett 8397 Apr 13 14:55 Breaker.java
-rw-rw-r-- 1 brett brett 1294 Sep 17 07:43 BruteMain.class
-rw-rw-r-- 1 brett brett 1910 Apr 13 14:56 BruteMain.java
-rw-rw-r-- 1 brett brett 2044 Apr 13 14:52 test.JCEKS

$ java BruteMain test.JCEKS 6 1
Breaking: test.JCEKS
Characters to test: 65
abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!_@

Starting search for depth: 6
tested 923174 pws (4 s -- 293634 pw/s): aadsbj

