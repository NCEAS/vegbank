#!/bin/sh 


# This script will drop then create the 
# concept based taxonomy database tables

if [ $# != 1 ] ; then
    echo 'Usage: [database type] {oracle postgresql} '
    echo ''
    echo ''
    exit 1
fi

DATABASE=$1
SQLDIR=../sql/

if test $DATABASE = postgresql
then
        echo "creating tables -- postgresql"
        psql nvc < $SQLDIR"createPlantTaxonDb_pg.sql"
else
        if  test $DATABASE = oracle
        then
        echo "creating tables -- oracle"
#				sqlplus nvc/jhhtpass @$CREATESCRIPT <<EOF
#				EOF
fi
fi




# Remove any existing tables
##sqlplus nvc/jhhtpass @$CREATESCRIPT <<EOF
##EOF

