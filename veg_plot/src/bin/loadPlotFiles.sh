#!/bin/sh

# This script can be used to load all the plot (xml) files
# in a directory into the plots database using the dbAccess
# tools 

ls -lrt outfile*.xml | awk '{print $9}' > list
cat list | while read line
do
echo "loading: " $line
java -classpath /home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/computer/harris/classtest/vegclass/veg_plot/src/oracleJDBC.jar:./ dbAccess $line plot2DBaddress.xsl insert
done

