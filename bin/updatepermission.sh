#!/bin/sh

# This script will update the permissions of a user from the 
# vegbank user profile database 
#	 '$Author: farrell $'
#  '$Date: 2003-07-15 17:19:34 $'
#  '$Revision: 1.1 $'


if [ "$#" -ne 3 ]
then
	echo " USAGE: updatepermission.sh <email> <host> <level>  "
	echo "  email: the email address of the user"
	echo "  host: the host machine like beta.nceas.ucsb.edu"
	echo "  levels: {1=registered 2=certified 3=professional 4=senior 5=manager}"
	exit 1
fi

ACTION='updatepermission'
EMAIL=$1
HOST=$2
LEVEL=$3

source ./setupCLASSPATH

java  -cp $CLASSPATH org.vegbank.common.utility.DatabaseUtility $ACTION $EMAIL $HOST $LEVEL
 
