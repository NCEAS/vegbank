#!/bin/sh -e

# this shell script will create the tables for the vegbank 
# plots database on the postgresql RDBMS using the psql 
# client
#
#*	'$Author: harris $'
#*  '$Date: 2002-03-26 18:57:40 $'
#*  '$Revision: 1.1 $'

#dropdb plots_dev
createdb plots_dev
psql plots_dev < create-vegbank-plots2002.sql
