#!/bin/sh -e 


#
# This script will build and load the protoype veg plot database -- it works when run on dev in a directory which has the 
# Oracle JDBC drivers installed
# 
# The script may be altered to include oracle drivers in the classpath
#




# Remove any existing tables
sqlplus harris/use4dev @removeTaxa.sql <<EOF
EOF

# Create the Tables, Views and Load some artificial data
sqlplus harris/use4dev @taxaDB.sql  <<EOF
EOF

#load the Juglandacea and Pinacea families - first separate the synonyms into two filesa (1996)

cat Pinaceae | grep '=' > Pinaceae.synon
cat Juglandaceae | grep '=' > Juglandaceae.synon

#First load the Juglandaceae Family
./loadTaxaPrototype.sh Juglandaceae name

#load the synonyms
./loadTaxaPrototype.sh Juglandaceae.synon correlation


#load the Pine Names
./loadTaxaPrototype.sh  Pinaceae name

#load the synonyms
./loadTaxaPrototype.sh Pinaceae correlation
