#!/bin/sh
#
# various bit of maintainence on the database

#run some periodic stuff, like removing old datasets
psql -U @databaseUser@ @databaseName@ < ~/vegbank/src/sql/periodic_cron_sql.sql

#rebuild cached pages on the homepage with ant:
ant -f ~/vegbank/web/build.xml cache