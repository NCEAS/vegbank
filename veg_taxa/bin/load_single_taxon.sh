#!/bin/sh  -e


DATADIR=../data/
INITIALDATA96=../data/plantlst1996.bin
INITIALDATA00=../data/plantlst2000.bin
TESTDATA96=../data/testlst1996.bin
TESTDATA00=../data/testlst2000.bin
XERCES=../lib/xerces_1_3_1.jar
XALAN=../lib/xalan_1_2_2.jar
JDBC=../lib/jdbc7.0-1.2.jar
PLANTTAXONMODULE=../build/lib/planttaxonomy.jar



CLASSPATH=$JDBC:$XALAN:$XERCES:$PLANTTAXONMODULE
longname="Verry Big Tree"
shortname="V B. Tree"
code="VBT"
taxondescription="A Very tree that was seen by the pacific ocean"
salutation="Mr."
givename="John"
surname="Harris"
orgname="NCEAS"
email="harris02@hotmail.com"
citationetails="VB20020530"
dateentered="2002-MAY-29"
usagestopdate="2005-MAY-29"
rank="GENUS"

echo "longname: " $longname
java   -classpath $CLASSPATH PlantTaxaLoader "$longname" "$shortname" "$code" "$taxondescription" "$salutation" "$givename" "$surname" "$orgname" "$email" "$citationdetails" "$dateentered" "$usagestopdate" "$rank"



