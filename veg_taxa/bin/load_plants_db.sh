#!/bin/sh -ev  


# This script will build and load the protoype plant 
#	taxonomy database 

DATADIR=../data/
INITIALDATA96=../data/plantlst1996.bin
INITIALDATA00=../data/plantlst2000.bin
TESTDATA96=../data/testlst1996.bin
TESTDATA00=../data/testlst2000.bin


CLASSPATH=/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:\
/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:\
/usr/local/devtools/jdk1.2.2/lib/rt.jar:\
/usr/local/devtools/jdk1.2.2/lib/dev.jar:\
/home/computer/harris/tools/oracleJDBC.jar:\
../lib/PlantTaxonomy.jar:./


# gunzip the plant data files (they are)
# gzip compressed in cvs
gunzip -r $DATADIR



# make a test data set from the 1996 and 2000
# plants list that contains just the Pineacea
# the Juglandacea and the Aster families.

grep Juglandacea  $INITIALDATA96 > $TESTDATA96
grep Pinaceae  $INITIALDATA96 >> $TESTDATA96
#grep Asteraceae   $INITIALDATA96 >> $TESTDATA96

grep Juglandacea  $INITIALDATA00 > $TESTDATA00
grep Pinaceae  $INITIALDATA00 >> $TESTDATA00
#grep Asteraceae   $INITIALDATA00 >> $TESTDATA00



# convert the plantlists to the xml format that they
# have to be in in order to load the database

java -Xmx700000000  -classpath $CLASSPATH USDAPlantsToXml $TESTDATA96
mv  outfile.xml $DATADIR/testPlantlst1996.bin.xml
java -Xmx700000000  -classpath $CLASSPATH USDAPlantsToXml $TESTDATA00
mv  outfile.xml $DATADIR/testPlantlst2000.bin.xml


#create the database tables, sequences triggers etc..
./create_taxa_db.sh


#First load the 1996  test data sets 
java -Xmx700000000  -classpath $CLASSPATH USDAPlantsLoader $DATADIR/testPlantlst1996.bin.xml
#load the 2000 test data set
java -Xmx700000000  -classpath $CLASSPATH  USDAPlantsLoader2000 $DATADIR/testPlantlst2000.bin.xml


# create the denormalized snapshot which will be used
# to run the quries against
./create_denormalized_view.sh



