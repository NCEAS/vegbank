#!/bin/sh   


# This script will build and load the protoype plant 
#	taxonomy database 

DATADIR=../taxaData/
INITIALDATA96=../taxaData/plantlst1996.bin
INITIALDATA00=../taxaData/plantlst2000.bin
TESTDATA96=../taxaData/testlst1996.bin
TESTDATA00=../taxaData/testlst2000.bin


XERCES=../lib/xerces_1_3_1.jar
XALAN=../lib/xalan_1_2_2.jar
JDBC=../lib/jdbc7.0-1.2.jar
PLANTTAXONMODULE=../lib/planttaxonomy.jar:


CLASSPATH=$JDBC:$XALAN:$XERCES:$PLANTTAXONMODULE




# gunzip the plant data files (they are)
# gzip compressed in cvs
#gunzip -r $DATADIR
#cp $INITIALDATA96  $TESTDATA96
#cp $INITIALDATA00  $TESTDATA00

# convert the plantlists to the xml format that they
# have to be in in order to load the database

#java -Xmx600M  -classpath $CLASSPATH USDAPlantsToXml $TESTDATA96
#mv  outfile.xml /tmp/testPlantlst1996.bin.xml


#First load the 1996  test data sets 
java -Xmx700M  -classpath $CLASSPATH USDAPlantsLoader outfile.xml




