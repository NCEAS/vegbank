#!/bin/sh

cd @build.test.integration.dir@ 
../../../exp/database_access/bin/load-native_xml.sh 

# Need to get data from database and write it to file
#psql plots_dev < getLastPlot.sql > lastPlotinBD.txt

# Need to get expected result 


#./compareInputToOutput.pl

# Need to compare both -- java 1.3.1 fails to complete this transformation due 
# stack overflow errors => use java 1.4.1
@java1.4.home.dir@/bin/java  -classpath @cpath@:../../lib/xalan_1_2_2.jar:\
../../lib/xerces_1_3_1.jar org.apache.xalan.xslt.Process   -XSL compareXML.xsl \
-IN ../TestData/native_xml_plot4.xml -OUT  @reports.test.dir@/TEST-PlotInsertion.xml \
-PARAM output @outputXML@
