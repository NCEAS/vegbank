#!/bin/sh
#
# Shell script for launching the desktop data management client
#
# Author: J. Harris
# Date: 3/08/2001 


XMLP=xerces_1_3_1.jar
XALAN=xalan_1_2_2.jar
VCLIENT=interface.jar
ACCESSMODULE=dbAccess.jar
CLIENT_COMMUNICATION=clientcommunication.jar
JDBC_PG=jdbc7.0-1.2.jar
JAVAX_JDBC=jta-spec1_0_1.jar
JDBC_INSTANTDB=idb.jar



if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$XMLP:$XALAN:$ACCESSMODULE:$CLIENT_COMMUNICATION:$JDBC_PG:$JDBC_INSTANTDB:$JAVAX_JDBC:$VCLIENT

#for i in *.jar
#do
#CLASSPATH=${CLASSPATH}:$i
#done

## convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi


echo $CLASSPATH
#java   -cp $CLASSPATH -jar $VCLIENT
java -verbose  -classpath  $CLASSPATH  MenuTest
