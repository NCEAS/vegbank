#!/bin/sh -e

# This script can be used to load the plots in 
# tnc plots file(s) this example loads the 
# tnc plots dataset
#
# Author: J. Harris
# Date: 3/08/2001
#

if [ "$#" -ne 2 ]
then
	echo "USAGE: loadTNCPlots.sh <sitefile> <speciesfile>  "
	echo "  sitefile: "
	echo "  speciesfile: "
	exit 1
fi

SITEFILE=$1
SPECIESFILE=$2

UTILITYMODULE=utility.jar
ACCESSMODULE=dbAccess.jar
PG_JDBC=jdbc7.0-1.2.jar
JDBC=oracleJDBC.jar
XALAN=lib/xalan_1_2_2.jar
XERCES=lib/xerces_1_3_1.jar





if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

CLASSPATH=$PG_JDBC:$JDBC:$XALAN:$XERCES:$ACCESSRESOURCE:$UTILITYMODULE

#for i in *.jar
#do
#CLASSPATH=${CLASSPATH}:$i
#done

## convert the existing path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#java   -cp $CPATH -jar $VCLIENT
# convert the existing path to windows


#java -cp dbAccess.jar;xalan_1_2_2.jar  dbAccess $XMLFILE $STYLESHEET $ACTION
java -cp $CLASSPATH TncConverter $SITEFILE $SPECIESFILE 

## arrange a list of the xml files
ls -lrt *.xml | awk '{print $9}'   > list


## load one file at a time
date > time
cat list | while read line
do
echo "loading: " $classpath

cat $line | sed 's/&//g' > tmp
mv tmp $line 

java -classpath dbAccess $line ../xml/vegPlot2001DBTrans.xsl insertPlot

done
date >> time
