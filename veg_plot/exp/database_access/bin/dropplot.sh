#!/bin/sh

# This script will delete a plot from the plots 
# database 
#	 '$Author: farrell $'
#  '$Date: 2003-02-03 18:41:06 $'
#  '$Revision: 1.2 $'

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
DATATRANSLATOR=../lib/datatranslator.jar
XMLRESOURCE=../lib/xmlresource.jar
PG_JDBC=../lib/jdbc7.0-1.2.jar
XALAN=../lib/xalan_1_2_2.jar
XERCES=../lib/xerces_1_4.jar
ACCESSRESOURCE=../lib/database_access.jar
UTILS=../lib/utilities.jar
PLANTTAXON=../lib/planttaxonomy.jar
CLASSPATH=$XMLRESOURCE:$DATATRANSLATOR:$PG_JDBC:$XALAN:$XERCES:$ACCESSRESOURCE:$UTILS:$PLANTTAXON


java  -cp $CLASSPATH databaseAccess.Utility $ACTION $PLOT $HOST 
