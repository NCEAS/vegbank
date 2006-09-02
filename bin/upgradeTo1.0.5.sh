#!/bin/sh
#
# updates the model to version 1.0.5
# first and only parameter is the database name
# 
# FIRST make sure cvs is updated!
# AND that build.properties is properly set to the right database
# You may want to create a new database, if so, see the next comments

echo "DB must exist before running this"
echo "if you only have a backup file, you could do this: "
echo "dropdb $1"
echo "createdb -U vegbank -E UNICODE $1"
echo "psql -U vegbank" $1 "\< THEbackupFile.sql  \>\> 105change.log 2\>&1"
cd ../

# now run some sql
echo "YOU MAY WANT TO tail -f 105change.log    in vegbank dir in a new terminal window"
echo "-------------------------------------------------------------------------------  " >> 105change.log
date   >> 105change.log
echo "starting upgrade to 1.0.5  " >> 105change.log
echo "-------------------------------------------------------------------------------  " >> 105change.log

echo "dropping views..."
echo "dropping views...  " >> 105change.log

psql  -U vegbank $1 < src/sql/drop_obsolete_views.sql  >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/drop_vegbank_views.sql   >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/drop_obsolete_views.sql   >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/drop_vegbank_views.sql   >> 105change.log 2>&1

echo "adding aggregates and extras..."
echo "adding aggregates and extras... " >> 105change.log
psql  -U vegbank $1 < src/sql/create_aggregrates.sql   >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/create_extras.sql   >> 105change.log 2>&1

echo "adding indexes..."
echo "adding indexes... " >> 105change.log
psql  -U vegbank $1 < src/sql/createIndices.sql   >> 105change.log 2>&1

echo "DOING THE CHANGES!..."
echo "DOING THE CHANGES!... " >> 105change.log
psql  -U vegbank $1 < src/sql/vegbank-changes-1.0.5.sql   >> 105change.log 2>&1

echo "creating VIEWS... "
echo "creating VIEWS...  " >> 105change.log
psql  -U vegbank $1 < src/sql/create_vegbank_views.sql   >> 105change.log 2>&1

echo "populate Config tables..."
echo "populate Config tables... " >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/vegbank_populate_configtables.sql   >> 105change.log 2>&1

echo "recreating any extra views..."
echo "recreating any extra views... " >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/create_vegbank_views.sql   >> 105change.log 2>&1

echo "NOW RUNNING ANT to populate data dictionary..."
echo "NOW RUNNING ANT to populate data dictionary... " >> 105change.log 2>&1
ant populateDD                             >> 105change.log 2>&1

echo "UPDATING PASSWORDS..."
echo "UPDATING PASSWORDS" >> 105change.log 2>&1
ant updateAllPasswords

echo "vacuuming..."
echo "vacuuming... " >> 105change.log 2>&1
psql  -U vegbank -c "vacuum analyze;" $1  >> 105change.log 2>&1

echo "getting stats..."
echo "getting stats... " >> 105change.log 2>&1
psql  -U vegbank $1 < src/sql/dba_current-table-stats.sql   >> 105change.log 2>&1

echo "NOW VERIFYING... you will see the output of this one"
echo "NOW VERIFYING... output was printed to screen " >> 105change.log 2>&1  
ant dbVerify