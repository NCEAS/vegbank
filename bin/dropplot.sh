#!/bin/sh

# This script will delete a plot from the plots 
# database 
#	 '$Author: farrell $'
#  '$Date: 2004-03-07 17:55:27 $'
#  '$Revision: 1.3 $'

if [ "$#" -ne 2 ]
then
	echo " USAGE: dropplot.sh <plotid> <host>  "
	echo "  plotid: the plot primary key value"
	echo "  host: the host machine like beta.nceas.ucsb.edu"
	echo "  "
	exit 1
fi


ACTION='dropplot'
PLOT=$1
HOST=$2
source @vegbank.home.dir@/bin/includes/setupCLASSPATH


java  -cp $CLASSPATH org.vegbank.common.utility.DatabaseUtility $ACTION $PLOT $HOST 
