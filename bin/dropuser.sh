#!/bin/sh

# This script will delete a user from the vegbank 
# user profile database 
#	 '$Author: farrell $'
#  '$Date: 2004-03-07 17:55:27 $'
#  '$Revision: 1.4 $'


if [ "$#" -ne 2 ]
then
	echo " USAGE: dropuser.sh <email> <host>  "
	echo "  email: the email address of the user"
	echo "  host: the host machine like beta.nceas.ucsb.edu"
	echo "  "
	exit 1
fi

ACTION='dropuser'
EMAIL=$1
HOST=$2

source @vegbank.home.dir@bin/includes/setupCLASSPATH

java  -cp $CLASSPATH org.vegbank.common.utility.DatabaseUtility $ACTION $EMAIL $HOST
 
