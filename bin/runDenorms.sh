#!/bin/sh
source @vegbank.home.dir@bin/includes/setupCLASSPATH
java -cp $CLASSPATH org.vegbank.common.utility.DenormUtility $1 
