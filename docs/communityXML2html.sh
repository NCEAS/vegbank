#!/bin/sh -e


# SHELL SCRIPT TO CONVERT THE XML DOCUMENTATION OF THE 
# VEGCLASS DATABASE PROJECT INTO HTML THAT WILL BE DISPLAYED 
# ON THE WEB SERVER
#     '$Author: harris $'
#     '$Date: 2002-06-17 21:00:49 $'
#     '$Revision: 1.2 $'


#	REMOVE AND RECREATE THE TARGET DIRECTORY
rm -rf dbdictionary/commtaxa
mkdir dbdictionary/commtaxa


 
ls -lt xml/commtaxa/*.xml | awk '{print $9}' > list


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
java -classpath $CLASSPATH:../../lib/xalan_1_2_2.jar:../../lib/xerces_1_3_1.jar  org.apache.xalan.xslt.Process -IN test  -XSL tableXml2Html.xsl  -out $OUTFILE


#move the html files to their respective directories
mv $OUTFILE dbdictionary/commtaxa/

done

rm list test
