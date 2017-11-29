#!/bin/sh
#
# decompile.sh
#
# Personal Data Security (PDS)
# Copyright 2016, PDS Software Solutions LLC - www.trustpds.com
#
# ##

# This script *MUST* be run as: $ ./decompile.sh
#
# For more information about this script, please see the README in
# this directory.
#
# ##


# Clean up from previous run:
rm -fr PDS2 pds.dec || exit

# Decompile
java -jar ./jd-core.jar pds.jar pds.dec || exit

# Strip EOL
sed -i '/^$/d' pds.dec/com/trustpds/*.java || exit

# Change to the decompiled classes directory
cd pds.dec/com/trustpds/ || exit

# List the (obfuscated) classes
ls

