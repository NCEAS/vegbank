#!/bin/sh

## this shell script will denormalize the veg plots
## database tables into a few summary tables that 
## are used for querying by the database access module

DATABASE_TYPE=@databaseType@
DATABASE_NAME=@databaseName@
USERNAME=@user@
PASSWORD=@password@

CREATE_ORACLE=../sql/vegPlot20001DBTables.sql
CREATE_POSTGRES=../sql/vegPlot20001DBTables_postgres.sql
DENORM_ORACLE=../sql/makePlotSummaryTables.sql
DENORM_POSTGRES=../sql/makePlotSummaryTables_postgres.sql

CLIENT_POSTGRES=psql
CLIENT_ORACLE=sqlplus


if [ "$#" -ne 1 ]
then
	echo "USAGE: <summarize> <create_tables>  "
	echo "  summarize: denormalizs the database structure - must be run before querying "
	echo "  create_tables: creates the database tables for the national vegetation plots db"
	exit 1
fi

ACTION=$1

# set the variables to do the 
# table denormalization

if test $ACTION = summarize
then
	echo "denormalizing tables"
	if test $DATABASE_TYPE = oracle
		then
		SQLSCRIPT=$DENORM_ORACLE
		SQL_CLIENT=$CLIENT_ORACLE
	fi
	if test $DATABASE_TYPE = postgresql
		then
		SQLSCRIPT=$DENORM_POSTGRES
		SQL_CLIENT=$CLIENT_POSTGRES
	fi
	echo $SQLSCRIPT
	
else 
	if test $ACTION = create_tables
		then
		echo "creating tables"
	if test $DATABASE_TYPE = oracle
		then
		SQLSCRIPT=$CREATE_ORACLE
		SQL_CLIENT=$CLIENT_ORACLE
	fi
	if test $DATABASE_TYPE = postgresql
		then
		SQLSCRIPT=$CREATE_POSTGRES
		SQL_CLIENT=$CLIENT_POSTGRES
	fi
	echo $SQLSCRIPT											
else
	echo "unrecognized command"
fi
fi

#echo $SQL_CLIENT

#issue the command to the database
if test  $DATABASE_TYPE = oracle
	then
	echo "sql client: "
	echo $SQL_CLIENT
	echo $SQLSCRIPT
	$SQL_CLIENT $USERNAME/$PASSWORD  < $SQLSCRIPT
fi

if test $DATABASE_TYPE = postgresql
	then
	$SQL_CLIENT $DATABASE_NAME  < $SQLSCRIPT
fi

