#!/bin/sh -e


echo "USAGE % tableXml2Html.sh <infile> <outfile>"

INFILE=$1
OUTFILE=$2

java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ org.apache.xalan.xslt.Process -IN $INFILE  -XSL tableXml2Html.xsl  -out $OUTFILE




