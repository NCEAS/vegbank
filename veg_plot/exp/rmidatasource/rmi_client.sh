#!/bin/sh -e

# This script runs the rmi client and specifically
# 1] passes an access data set to the rmi server
# 2] loads the first plot in the archive into the VegBank System
#*  Authors: @author@
#*  Release: @release@
#*	
#*  '$Author: harris $'
#*  '$Date: 2002-07-25 16:47:19 $'
#* 	'$Revision: 1.5 $'
##


if [ "$#" -ne 4 ]
then
  echo "USAGE: rmi_client.sh <host> <Access file> <filetype> <plot>  "
	echo "  host: the host onwhich the server is running"
	echo "  Access file: the path and name of the TNC plots file"
	echo "  filetype: the type of plots file: 'tnc', 'vbaccess', 'nativexml'"
	echo "  plot: the plot name or '-all' "
	echo "   "
	exit 1
fi																				

HOST=$1
FILE=$2
TYPE=$3
PLOT=$4

java -Xmx700M  -classpath ./lib/xml-apis.jar:./lib/xerces_1_4.jar:./lib/xalan_1_2_2.jar:./lib/xmlresource.jar:./lib/datatranslator.jar:./:./lib/rmidatasource.jar  \
-Djava.security.policy=./lib/policy.txt DataSourceClient $1 $2 $3 $4 

