#!/bin/sh 
#
# Takes a database name and a database create script name as parameters.
# Drops the database and rebuilds it using the sql script specified.
#
# Currently Postgres specific.
#

DATABASE=$1


#echo $DATABASE --- $DBCREATESCRIPT
dropdb $DATABASE
createdb $DATABASE

shift

until [ -z "$1" ]
do 
  psql $DATABASE < $1 
  echo -n "$1"
  shift
done

exit 0

