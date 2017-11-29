# README.Decompile.txt
# 
# Personal Data Security (PDS)
# Copyright 2016, PDS Software Solutions LLC - www.trustpds.com
# 
# ##


In accordance with the EULA, you are allowed to decompile the PDS
application.  In point of fact, decompiling PDS is encouraged.

The PDS application may be easily decompiled using the script
in this directory.

The script uses the Java decompiler available at Github:

  https://github.com/java-decompiler/jd-gui/releases

These instructions use the CLI version of the decompiler, not the GUI.

The CLI decompiler is:  jd-core.jar


To decompile the PDS jar, follow these steps:

  1.  Change to the ".../tools/decompile/" directory.

  2.  Copy the decompiler (jd-core.jar) to the ".../tools/decompile" directory.

  3.  Copy the PDS JAR file (pds.jar) to the ".../tools/decompile" directory.

  4.  Execute:  $ ./decompile.sh


Notes:

    Errors may show up during the decompile of an obfuscated JAR.
    Errors are not seen when decompiling a non-obfuscated JAR.

    Decompiled classes will be in the directory pds.dec/com/trustpds/*
    The Java source files should be listed by the decompile script.

    Review the decompiled classes.
      1. Search for things that should not be seen in this application.
         - Networking system calls - connect(), socket(), etc.
         - Other...

