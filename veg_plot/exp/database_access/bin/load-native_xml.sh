#!/bin/sh -ev

INFILE=../data/native-xml-test.xml
PLOT=Fern-1
# copy the infile to the target file
cp $INFILE ./input.xml

java -classpath ../lib/utilities.jar:../lib/datatranslator.jar:../lib/jdbc7.0-1.2.jar:../lib/database_access.jar:../lib/xerces_1_4.jar:../lib/xalan_1_2_2.jar:../lib/xmlresource.jar:../lib/planttaxonomy.jar databaseAccess.DBinsertPlotSource NativeXmlPlugin $PLOT
# remove the plot cache
rm -rf plot_cache
