#!/bin/sh 



java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ org.apache.xalan.xslt.Process -IN tableExample.xml  -XSL tableXml2Html.xsl  -out outFile.html


java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:/home/computer/harris/java/xml/xalan_1_0_0/xalan.jar:/home/computer/harris/java/xml/xalan_1_0_0/xerces.jar:./ org.apache.xalan.xslt.Process -IN tableExample.xml  -XSL tableXml2Html.xsl  




