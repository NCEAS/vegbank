#!/bin/sh

# This script will  load an XML into vegbank
#  '$Author: farrell $'
#  '$Date: 2004-02-18 02:02:08 $'
#  '$Revision: 1.1 $'

source /usr/vegbank/bin/includes/setupCLASSPATH

java  -cp $CLASSPATH org.vegbank.plots.datasource.VegbankXMLUpload $*
