#!/bin/sh -e

# This shell script will load the concept-based veg community database by
# performing the following tasks:
# 1] dropping the ecoart tables
# 2] create the ecoart tables on the server
# 3] load the ecoart data into their respective tables
# 4] create the tables for the concept-based community database
# 5] migrate those data to the concept-based community tables
# 6] for querying ease take a 'snap-shot' of the community data into a single
# denormalized table called community summary 'commSummary' 


#first drop the tables that are identical to those in ecoart
sqlplus harris/use4dev @../sql/dropEcoartTables.sql << EOF
EOF

#first create the tables identical to those in ecoart
sqlplus harris/use4dev @../sql/createEcoartTables.sql << EOF
EOF


#load all but the alliance ecoart data to the tables -- for some reason there
# has been a problem using sqlloader to load the alliance data specifically
# loading the description data.
./loadEcoartTables.load

# load the missing alliance data using a java-JDBC application
./loadEcoartAllianceTable.sh

#create and migrate the data to the concept-based database
sqlplus harris/use4dev @../sql/migrateEcoart.sql << EOF
EOF

#create the denormalized table used for querying
sqlplus harris/use4dev @../sql/createCommunitySummary.sql << EOF
EOF


