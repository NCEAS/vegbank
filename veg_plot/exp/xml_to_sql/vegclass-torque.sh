#!/bin/sh

# SHELL SCRIPT THAT WILL CONVERT THE VEGCLASS-CONSISTENT XML
# TO KAJARTA-TORQUE CONSISTENT XML FOR CONVERSION TO RDBMS 
# SPECIFIC SQL
#
#     '$Author: harris $'
#     '$Date: 2001-10-17 17:28:27 $'
#     '$Revision: 1.3 $'


# the directory that contains the documentation for the vegplot database
PLOTDOCDIR=./data/vegclass_doc/

# transform the xml to the torqu-xml
java -classpath ./lib/xalan_1_2_2.jar:./lib/xerces_1_4.jar:./lib/xmltosql.jar \
VegclassXMLDoc  $PLOTDOCDIR

# copy the output to he torque directory
cp test_fix.xml ./torque/schema/project-schema.xml

# move into the torque directory and execute ant -- to handle the transformation 
# to sql
cd torque
ant
cp src/sql/project-schema.sql ../output.sql 
