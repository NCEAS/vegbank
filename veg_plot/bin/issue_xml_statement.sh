#!/bin/sh

#
# This shell script starts the nvc database
# access module allowing a user to issue a 
# statement (either a query or insert statement)
# that is embedded in an xml file 
#
# Author: J. Harris
# Date: 3/08/2001
#

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
  if  test $ACTION = verify
	then
 	echo "issuing a database insert -- plot insertion"
	STYLESHEET=../xml/vegPlot2001DBTrans.xsl			
else
	echo "unrecognized command"
fi
fi
fi


ACCESSRESOURCE=dbAccess.jar
ACCESSMODULE=dbAccess.jar
#postgres jdbc drivers
PG_JDBC=jdbc7.0-1.2.jar
JDBC=oracleJDBC.jar
XALAN=xalan_1_2_2.jar
XERCES=xerces_1_3_1.jar
ENHYDRA=idb.jar
JAVAX=jta-spec1_0_1.jar

if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$PG_JDBC:$JDBC:$XALAN:$XERCES:$ACCESSRESOURCE:$ENHYDRA:$JAVAX

#for i in *.jar
#do
#CLASSPATH=${CLASSPATH}:$i
#done

## convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#java   -cp $CPATH -jar $VCLIENT
# convert the existing path to windows

echo $CLASSPATH
#java -cp dbAccess.jar;xalan_1_2_2.jar  dbAccess $XMLFILE $STYLESHEET $ACTION
java -cp $CLASSPATH dbAccess $XMLFILE $STYLESHEET $ACTION 
