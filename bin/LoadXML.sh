#!/bin/sh

# This script will  load an XML into vegbank
#  '$Author: farrell $'
#  '$Date: 2004-03-07 17:55:27 $'
#  '$Revision: 1.2 $'

source @vegbank.home.dir@/bin/includes/setupCLASSPATH

java  -cp $CLASSPATH org.vegbank.dataload.XML.VegbankXMLUpload $*
