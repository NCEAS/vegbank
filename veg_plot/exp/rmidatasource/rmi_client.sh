#!/bin/sh -e

# This script runs the rmi client and specifically
# 1] passes an access data set to the rmi server
# 2] loads the first plot in the archive into the VegBank System
#*  Authors: @author@
#*  Release: @release@
#*	
#*  '$Author: harris $'
#*  '$Date: 2002-03-29 16:22:23 $'
#* 	'$Revision: 1.3 $'
##


if [ "$#" -lt 2 ]
then
  echo "USAGE: rmi_client.sh <host> <Access file> {plot, -all}  "
	echo "  host: the host onwhich the server is running"
	echo "  Access file: the path and name of the TNC plots file"
	echo "	optional: the plot name or '-all' "
	exit 1
fi																				

HOST=$1
FILE=$2
PLOT=$3

java -classpath ./lib/xmlresource.jar:./lib/datatranslator.jar:./:./lib/rmidatasource.jar  \
-Djava.security.policy=./lib/policy.txt DataSourceClient $1 $2 $3 

