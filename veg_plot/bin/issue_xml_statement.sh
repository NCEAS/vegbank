#!/bin/sh
#
# This shell script starts the nvc database
# access module allowing a user to issue a 
# statement (either a query or insert statement)
# that is embedded in an xml file 
#
# Author: J. Harris
# Date: 3/08/2001 

STATEMENTXML=$1
STYLESHEET=../xml/querySpec.xsl
ACCESSRESOURCE=../lib/dbAccess.jar
ACCESSMODULE=../lib/dbAccess.jar
#ACCESSMODULE=../src/
JDBC=../lib/oracleJDBC.jar
XALAN=../lib/xalan.jar
XERCES=../lib/xerces.jar


if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$JDBC:$XALAN:$XERCES:$ACCESSRESOURCE

# convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#java   -cp $CPATH -jar $VCLIENT
# convert the existing path to windows

echo $CLASSPATH

#java -classpath ${CLASSPATH}  -jar  $ACCESSMODULE $STATEMENTXML $STYLESHEET $2

java -cp $CLASSPATH dbAccess $1 $2 $3 
