#!/bin/sh

#
# This shell is to be used to create the veg plot
# database tables on the Enhydra / InstantDb backend
# and to create and update the summary tables on the 
# instantDB backend
#
# Author: J. Harris
# Date: 5/08/2001
#

if [ "$#" -ne 1 ]
then
	echo "USAGE: local_database_management.sh  <action>  "
	echo "  actions may include: createBaseTables, createSummaryTables, updateSummary"
	echo "  "
	exit 1
fi

ACTION=$1


ACCESSRESOURCE=dbAccess.jar
ACCESSMODULE=dbAccess.jar
#postgres jdbc drivers
PG_JDBC=jdbc7.0-1.2.jar
JDBC=oracleJDBC.jar
XALAN=xalan_1_2_2.jar
XERCES=xerces_1_3_1.jar
ENHYDRA=idb.jar
JAVAX=jta-spec1_0_1.jar
EMBEDEDDB=embedded_db.jar

if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$EMBEDEDDB:$PG_JDBC:$JDBC:$XALAN:$XERCES:$ACCESSRESOURCE:$ENHYDRA:$JAVAX

#for i in *.jar
#do
#CLASSPATH=${CLASSPATH}:$i
#done

## convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

# convert the existing path to windows

echo $CLASSPATH
#java -cp dbAccess.jar;xalan_1_2_2.jar  dbAccess $XMLFILE $STYLESHEET $ACTION
java -cp $CLASSPATH LocalDatabaseManager  $ACTION 
