#!/bin/sh -v

# this script will process the xml data to standard out
# to change the output to a file use the -OUT 'file' flag
# must also have included in the the class path both the 
# XML parser and processor like the following line:
# set CLASSPATH=%CLASSPATH%;C:\JAVA;c:\xml\xalan_1_0_0\bsfengines.jar;
# c:\xml\xalan_1_0_0\bsf.jar;c:\xml\xalan_1_0_0\xerces.jar;
# c:\xml\xalan_1_0_0\xalan.jar;c:\xml\xalan_1_0_0\xalansamples.jar;
# c:\xml\xerces-1_1_2\xercesSamples.jar

#make sure that the coding in the header of the xml file is not more
#sophisticated than the parser/processor
 

java org.apache.xalan.xslt.Process -IN servlet_output.file.xml  -XSL veg_desc_flat.xsl -OUT test 
