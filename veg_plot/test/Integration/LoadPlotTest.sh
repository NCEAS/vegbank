#!/bin/sh  

export INFILE=../testdata/nativeDataSource.xml
export PLOT=Fern-1
OUTFILE=out.xml

cd @build.test.integration.dir@ 

###############################
# Load a plot
###############################
 
# copy the infile to the target file
cp $INFILE ./input.xml
 
java databaseAccess.DBinsertPlotSource NativeXmlPlugin $PLOT

# remove the plot cache
rm -rf plot_cache

###############################
# Get the plotId 
###############################
PLOTID=`psql plots_dev < getLastPlotId.sql | sed '3!d'`
echo $PLOTID


###############################
# Retrieve the Plot from DB
###############################
# Need to get data from database and write it to a file
@java1.4.home.dir@/bin/java databaseAccess.dbAccess $PLOTID $OUTFILE writeSingleVegBankPlot

###############################
# Compare the Input to Output
###############################
# Need to compare both -- java 1.3.1 fails to complete this transformation due 
# stack overflow errors => use java 1.4.1
@java1.4.home.dir@/bin/java org.apache.xalan.xslt.Process   -XSL compareXML.xsl \
-IN $INFILE -OUT  @reports.test.dir@/TEST-PlotInsertion.tmp -PARAM output $OUTFILE  #@outputXML@


###############################
# Post Process the XML Report to
# Conform to Junit standard
###############################
# Process the output xml so it follows the junit standard
#@java1.4.home.dir@/bin/java  -classpath @cpath@:../../lib/xalan_1_2_2.jar:\
#../../lib/xerces_1_3_1.jar org.apache.xalan.xslt.Process   -XSL generateJunitXML.xsl \
#-IN @reports.test.dir@/TEST-PlotInsertion.tmp -OUT  @reports.test.dir@/TEST-PlotInsertion.xml

@java1.4.home.dir@/bin/java  org.apache.xalan.xslt.Process -XSL generateJunitXML.xsl \
-IN @reports.test.dir@/TEST-PlotInsertion.tmp \
-OUT  @reports.test.dir@/TEST-PlotInsertion.xml

rm @reports.test.dir@/TEST-PlotInsertion.tmp 
