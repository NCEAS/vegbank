#!/bin/sh

# SHELL SCRIPT THAT WILL CONVERT THE VEGCLASS-CONSISTENT XML
# TO KAJARTA-TORQUE CONSISTENT XML FOR CONVERSION TO RDBMS 
# SPECIFIC SQL
#
#     '$Author: harris $'
#     '$Date: 2001-10-17 17:11:12 $'
#     '$Revision: 1.2 $'


PROJECT=./data/vegclass_doc/vegPlotProject.xml
PLOT=./data/vegclass_doc/vegPlotPlot.xml
PLOTDOCDIR=./data/vegclass_doc/

java -classpath ./lib/xalan_1_2_2.jar:./lib/xerces_1_4.jar:./lib/xmltosql.jar \
VegclassXMLDoc  $PLOTDOCDIR



#java -classpath ./lib/xalan_1_2_2.jar:./lib/xerces_1_4.jar:./lib/xmltosql.jar \
#VegclassXMLDoc  $PLOT
#cat test_fix.xml >> ./data/torque_xml/nvc-database.xml


cp test_fix.xml ./torque/schema/project-schema.xml


