#!/bin/sh  

INFILE=$1
PLOT=$2
OUTFILE=$3
PLUGIN=$4
TESTNAME=$5
RMISERVER=$6
EXPECTEDFILE=$7

#export INFILE=../testdata/nativeDataSource.xml
#export PLOT=Fern-1
#OUTFILE=out.xml

cd @build.test.integration.dir@ 

###############################
# Load a plot
###############################
 
# copy the infile to the target file
cp $INFILE ./input.xml
 
case $PLUGIN in
  NativeXmlPlugin)
    #java databaseAccess.DBinsertPlotSource $PLUGIN $PLOT
    java org.vegbank.common.utility.DataLoader db vegbank localhost $INFILE 
    ;;
  *)
    java org.vegbank.plots.rmi.DataSourceClient $RMISERVER $INFILE $PLUGIN $PLOT
esac    

# remove the plot cache
rm -rf plot_cache

###############################
# Get the plotId 
###############################
PLOTID=`psql vegbank < getLastPlotId.sql | sed '3!d'`
echo PLOTID is $PLOTID


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
-IN $EXPECTEDFILE -OUT  @reports.test.dir@/TEST-$TESTNAME.tmp \
-PARAM output $OUTFILE -PARAM testName $TESTNAME


###############################
# Post Process the XML Report to
# Conform to Junit standard
###############################
# Process the output xml so it follows the junit standard
#@java1.4.home.dir@/bin/java  -classpath @cpath@:../../lib/xalan_1_2_2.jar:\
#../../lib/xerces_1_3_1.jar org.apache.xalan.xslt.Process   -XSL generateJunitXML.xsl \
#-IN @reports.test.dir@/TEST-$TESTNAME.tmp -OUT  @reports.test.dir@/TEST-$TESTNAME.xml

@java1.4.home.dir@/bin/java  org.apache.xalan.xslt.Process -XSL generateJunitXML.xsl \
-IN @reports.test.dir@/TEST-$TESTNAME.tmp \
-OUT  @reports.test.dir@/TEST-$TESTNAME.xml

rm @reports.test.dir@/TEST-$TESTNAME.tmp 
