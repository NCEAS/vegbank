#!/bin/sh

ls -lt vegPlot*.xml | awk '{print $9}' > list

cat list |
while read line
do

echo $line | sed 's/.xml/.html/g' > tmp
read OUTFILE<tmp
rm tmp


#update the date attribute in the xml and then write it to the rtest file
java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:/home/computer/harris/java:./  updateXML $line test


#convert to the html
java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ org.apache.xalan.xslt.Process -IN test  -XSL tableXml2Html.xsl  -out $OUTFILE


#java org.apache.xalan.xslt.Process -IN $line  -XSL tableXml2Html.xsl  -out $OUTFILE

#move the html files to their respective directories
mv $OUTFILE ../veg_plot/docs/html/

done

