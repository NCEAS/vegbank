#!/bin/sh

#
# General utility for transforming an xml with an xsl style sheet
# 


if [ "$#" -ne 2 ]
then
	echo "USAGE: transformXML.sh [xmlFile] [xslFile]"
	echo "USAGE: transformXML.sh [xmlFile] [xslFile] > outputFile"
	exit 1
fi	

CLASSPATH=/usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ 
XMLFILE=$1
XSLTFILE$2


java -cp $CLASSPATH org.apache.xalan.xslt.Process -IN $1  -XSL $2 




