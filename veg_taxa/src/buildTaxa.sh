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

#compile the java code
#javac loadPlants.java

#/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadPlants test2 



