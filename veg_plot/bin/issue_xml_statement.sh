#!/bin/sh

#
# This shell script starts the nvc database
# access module allowing a user to issue a 
# statement (either a query or insert statement)
# that is embedded in an xml file 
#
# Author: J. Harris
# Date: 3/08/2001


if [ "$#" -ne 2 ]
then
	echo "USAGE: issue_xml_statement.sh <xmlfile> <action>  "
	echo "  action may include: query, insert, insertPlot"
	echo "  or: compoundQuery, simpleCommunityQuery, verify"
	exit 1
fi

XMLFILE=$1
ACTION=$2

if test $ACTION = query 
then
	echo "issuing a database query"
	STYLESHEET=../xml/querySpec.xsl
else
	if  test $ACTION = insertPlot
	then
	echo "issuing a database insert -- plot insertion"
	STYLESHEET=../xml/vegPlot2001DBTrans.xsl
else
	echo "unrecognized command"
fi
fi

ACCESSRESOURCE=../lib/dbAccess.jar
ACCESSMODULE=../lib/dbAccess.jar
JDBC=../lib/oracleJDBC.jar
XALAN=../lib/xalan.jar
XERCES=../lib/xerces.jar


if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$JDBC:$XALAN:$XERCES:$ACCESSRESOURCE

## convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#java   -cp $CPATH -jar $VCLIENT
# convert the existing path to windows


java -cp $CLASSPATH dbAccess $XMLFILE $STYLESHEET $ACTION 
