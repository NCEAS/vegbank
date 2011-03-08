#!/bin/sh
#
# various bit of maintenance on the database

echo ----------------------------------------------
echo STARTING THE MAINTENANCE FILE AT:
date

#run some periodic stuff, like removing old datasets
psql -U @databaseUser@ @databaseName@ < ~/vegbank/src/sql/periodic_cron_sql.sql

#rebuild cached pages on the homepage with ant:
# ant isn't working with cron, so we are just running these manually
#ant -f ~/vegbank/web/build.xml cache

## these are now cached data in the database itself, wget is not needed any more
## wget -nv -O @webapps_dir@/vegbank/cache/views/raw/raw_countdata.jsp           http://@machineURL@/vegbank/views/raw/raw_countdata.jsp
## wget -nv -O @webapps_dir@/vegbank/cache/views/raw/raw_recentprojects.jsp      http://@machineURL@/vegbank/views/raw/raw_recentprojects.jsp

echo DONE WITH MAINTENANCE AT:
date
echo ----------------------------------------------