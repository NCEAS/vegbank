#!/bin/sh


# SHELL SCRIPT TO CONVERT THE XML DOCUMENTATION OF THE 
# VEGCLASS DATABASE PROJECT INTO HTML THAT WILL BE DISPLAYED 
# ON THE WEB SERVER

 
ls -lt vegPlot*.xml | awk '{print $9}' > list

cat list |
while read line
do

echo $line | sed 's/.xml/.html/g' > tmp
read OUTFILE<tmp
rm tmp


#update the date attribute in the xml and then write it to the rtest file
#java  updateXML $line test
cp $line test

#convert to the html
java -classpath $CLASSPATH:./lib/xalan_1_2_2.jar:./lib/xerces_1_3_1.jar  org.apache.xalan.xslt.Process -IN test  -XSL vegclass-turbine.xsl  -out $OUTFILE


#move the html files to their respective directories
mv $OUTFILE ./turbine/

done

rm list test
