#!/bin/sh -e

# This script runs the rmi client and specifically
# 1] passes an access data set to the rmi server
# 2] loads the first plot in the archive into the VegBank System
#
#

if [ "$#" -ne 2 ]
then
  echo "USAGE: rmi_client.sh <host> <Access file>  "
	echo "  host: the host onwhich the server is running"
	echo "  Access file: the path and name of the TNC plots file"
	exit 1
fi																				

HOST=$1
FILE=$2

java -classpath ./lib/datatranslator.jar:./:./lib/rmidatasource.jar  \
-Djava.security.policy=./lib/policy.txt DataSourceClient $1 $2 

