#!/bin/sh -e 


#
# This script will build and load the protoype veg plot database -- it works when run on dev in a directory which has the 
# Oracle JDBC drivers installed
# 
# The script may be altered to include oracle drivers in the classpath
#




# Remove any existing tables
sqlplus harris/use4dev @dropVegPlotTables.sql <<EOF
EOF

# Create the Tables, Views and Load some artificial data
sqlplus harris/use4dev @vegPlotTables.sql <<EOF
EOF


end 




# Verify that the tables are on the DB
/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadTNCspecies -Q

# Load the tnv plot data first
/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadTNCplot -plot tncYosemitePlotsInput
/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadTNCplot -plot tncSamplePlotsInput

# Load the tnc species data associate with the plot data loaded above
/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadTNCspecies -species tncYosemiteSpeciesInput
/usr/local/devtools/jdk1.2.2/bin/java -classpath /usr/local/devtools/jdk1.2.2/lib/rt.jar:/usr/local/devtools/jdk1.2.2/lib/dev.jar:./oracleJDBC.jar:./ loadTNCspecies -species tncSampleSpeciesInput

# send a query to the DB
sqlplus harris/use4dev @queryByplot.sql <<EOF
EOF


