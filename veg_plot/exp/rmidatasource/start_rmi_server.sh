#!/bin/sh

CP=lib/plotvalidator.jar:lib/database_access.jar:lib/planttaxonomy.jar:lib/rmidatasource.jar:lib/xerces_1_4.jar:lib/xalan_1_2_2.jar:lib/xmlresource.jar:lib/utilities.jar:lib/jdbc7.0-1.2.jar:lib/datatranslator.jar:./:lib/vegclass_common.jar:$CLASSPATH  
echo $CP 
java -classpath $CP  -Djava.security.policy=./lib/policy.txt org.vegbank.plots.rmi.DataSourceServer
