#!/bin/sh

source /usr/vegbank/bin/includes/setupCLASSPATH

echo $CLASSPATH

datasink=$1
database=$2
databasehost=$3
datasource=$4


java -cp $CLASSPATH org.vegbank.common.utility.DataLoader $datasink $database \
$databasehost $datasource
