#!/bin/sh -e

# this shell script will create the tables for the vegbank 
# plots database on the postgresql RDBMS using the psql 
# client
#
#*	'$Author: farrell $'
#*  '$Date: 2003-05-29 00:14:08 $'
#*  '$Revision: 1.3 $'

dropdb vegbank
createdb vegbank
psql vegbank < create-vegbank-plots2002.sql
