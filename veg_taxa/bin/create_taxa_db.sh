#!/bin/sh -ev  


# This script will drop then create the 
# concept based taxonomy database tables

CREATESCRIPT=../lib/createPlantTaxonDb.sql

# Remove any existing tables
sqlplus nvc/jhhtpass @$CREATESCRIPT <<EOF
EOF


