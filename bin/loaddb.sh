#!/bin/sh 
#
# Runs a list of SQL files against the Database.
#
# Currently Postgres specific.
#

DATABASE=$1

shift

until [ -z "$1" ]
do 
  psql $DATABASE < $1 
  echo -n "$1"
  shift
done

exit 0

