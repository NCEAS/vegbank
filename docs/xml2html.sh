#!/bin/sh -e 

ls -lt vegPl*.xml | awk '{print $9}' > list
#ls -lrt plantTaxa*.xml | awk '{print $9}' > list

cat list |
while read line
do

echo $line | sed 's/.xml/.html/g' > tmp
read OUTFILE<tmp
rm tmp

java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ org.apache.xalan.xslt.Process -IN $line  -XSL tableXml2Html.xsl  -out $OUTFILE




done

