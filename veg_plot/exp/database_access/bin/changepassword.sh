#!/bin/sh

# This script will change a user's password in the profile 
# database
#	 '$Author: harris $'
#  '$Date: 2002-08-30 18:25:17 $'
#  '$Revision: 1.1 $'


if [ "$#" -ne 3 ]
then
	echo " USAGE: changepassword.sh <email> <host> <newpassword>  "
	echo "  email: the email address of the user"
	echo "  host: the host machine like beta.nceas.ucsb.edu"
	echo "  newpassword: a new password "
	exit 1
fi

ACTION='changepassword'
EMAIL=$1
HOST=$2
LEVEL=$3


DATATRANSLATOR=../lib/datatranslator.jar
XMLRESOURCE=../lib/xmlresource.jar
PG_JDBC=../lib/jdbc7.0-1.2.jar
XALAN=../lib/xalan_1_2_2.jar
XERCES=../lib/xerces_1_4.jar
ACCESSRESOURCE=../lib/database_access.jar
UTILS=../lib/utilities.jar
PLANTTAXON=../lib/planttaxonomy.jar
CLASSPATH=$XMLRESOURCE:$DATATRANSLATOR:$PG_JDBC:$XALAN:$XERCES:$ACCESSRESOURCE:$UTILS:$PLANTTAXON

java  -cp $CLASSPATH databaseAccess.utility $ACTION $EMAIL $HOST $LEVEL
 
