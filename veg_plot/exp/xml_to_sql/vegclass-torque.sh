#!/bin/sh

# SHELL SCRIPT THAT WILL CONVERT THE VEGCLASS-CONSISTENT XML
# TO JAKARTA-TORQUE CONSISTENT XML FOR CONVERSION TO RDBMS 
# SPECIFIC SQL
#
#     '$Author: harris $'
#     '$Date: 2002-06-17 17:59:44 $'
#     '$Revision: 1.7 $'


# the directory that contains the documentation for the vegplot database
PLOTDOCDIR=./data/vegclass_doc/
TAXONOMYDOCDIR=./data/vegclass_doc/

if [ "$#" -ne 1 ]
then
	echo "USAGE: vegclass-torque.sh <action>  "
	echo "  action may include: plot, plant-taxonomy, comm-taxonomy"
	exit 1
fi

ACTION=$1

#select which module

if test $ACTION = plot 
then
	TARGETPACKAGE=$PLOTDOCDIR
	echo  $TARGETPACKAGE
else
	if  test $ACTION = comm-taxonomy
	then
	TARGETPACKAGE=$TAXONOMYDOCDIR
	echo  $TARGETPACKAGE
else
	if  test $ACTION = plant-taxonomy
	then
	TARGETPACKAGE=$TAXONOMYDOCDIR
	echo  $TARGETPACKAGE
fi
fi
fi



# transform the xml to the torque-xml
java -classpath ./lib/xmlresource.jar:./lib/xalan_1_2_2.jar:./lib/xerces_1_4.jar:./lib/xmltosql.jar \
VegclassXMLDoc  $TARGETPACKAGE $ACTION

# copy the output to he torque directory
cp test_fix.xml ./torque/schema/project-schema.xml

# move into the torque directory and execute ant -- to handle the transformation 
# to sql
cd torque
ant project-sql
cp src/sql/project-schema.sql ../$ACTION.sql 
echo $ACTION.sql written
