#!/bin/sh

# shell script to start the work flow manager

java -classpath $CLASSPATH:./lib/hsqldb.jar:./lib/vegClient.jar:./lib/jdbc7.0-1.2.jar:./lib/xalan_merge.jar:./lib/xalan.jar:./lib/xerces_1_4.jar:./lib/xmloperator.jar:./lib/xerces_1_3_1.jar:./lib/xalan_1_2_2.jar:./lib/database_access.jar vegclient.framework.VegClient

