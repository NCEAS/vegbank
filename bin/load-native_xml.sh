#!/bin/sh -ev

export INFILE=../data/native-xml-test.xml
export PLOT=Fern-1

cd $VEGBANK_HOME/veg_plot/exp/database_access/bin

# copy the infile to the target file
cp $INFILE ./input.xml

source /usr/vegbank/bin/includes/setupCLASSPATH

java -classpath $CLASSPATH databaseAccess.DBinsertPlotSource NativeXmlPlugin $PLOT
# remove the plot cache
rm -rf plot_cache
