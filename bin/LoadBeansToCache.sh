#!/bin/sh

# This script will populate the cache
#  '$Author: farrell $'
#  '$Date: 2004-03-07 17:55:27 $'
#  '$Revision: 1.1 $'

source @vegbank.home.dir@/bin/includes/setupCLASSPATH

java  -cp $CLASSPATH org.vegbank.common.utility.CommandLineTools.LoadBeansToCache $*
