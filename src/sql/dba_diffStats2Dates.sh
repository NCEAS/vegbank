#!/bin/sh 
#
# Takes a database name and a database create script name as parameters.
# Drops the database and rebuilds it using the sql script specified.
#
# Currently Postgres specific.
#

if [ $# -eq 0 ]
then

  echo 'usage: dba_diffStats2Dates.sh "DATE1" [ALL] [DATABASE] ["DATE2"] '
  echo "DATE1 is the early date for which comparison of #'s of records and max PK's is calculated - use double quotes around it"
  echo "ALL is true if you want to show all records from comparison, else shows just those tables that changed."
  echo 'DATABASE is vegbank if omitted'
  echo 'DATE2 is now() if omitted - use double quotes otherwise'
  echo 'examples: ./dba_diffStats2Dates.sh "2005-03-30 15:45:02"       (this shows diffs b/t now and Mar 30, 2005 in vegbank db)'
  echo '          ./dba_diffStats2Dates.sh "2005-03-30 15:45:02" true vegtest  "2005-06-15"     (this shows stats for tables b/t June 15, 2005 and Mar 30, 2005 in vegtest db)'
  exit 1
fi

DATE1=$1

ALL=$2
if [ "$ALL" = "true" ]
then
  CRITERIA=""  
  DIFFS = "Stats"
else
  CRITERIA=" HAVING  (late.countrecs - early.countrecs) <> 0 "
  DIFFS = "Differences"
fi

DATE2=$4

if [ $# -lt 4 ] 
then
  
 DATE2=`date +"%Y-%m-%d\ %H:%M:%S"`

fi

DATABASE=$3

if [ $# -lt 3 ]
then

  DATABASE='vegbank'
fi


echo "$DIFFS for $DATABASE between (earlier) $DATE1 and (later) $DATE2"

psql -U datauser $DATABASE -c "SELECT late.stattable,  early.countrecs as earlyCountRecs,        early.maxPK as earlyMaxPK,       late.countrecs as lateCountRecs,       late.maxPK as lateMaxPK,       (late.countrecs - early.countrecs) as diffCount                  FROM dba_dbstatstime as early,         (select max(stat_id) as sid, stattable from dba_dbstatstime where statdate<'$DATE1' group by stattable) as earlyID,        dba_dbstatstime as late,         (select max(stat_id) as sid, stattable from dba_dbstatstime where statdate<'$DATE2' group by stattable) as lateID     where earlyID.sID=early.stat_ID AND lateID.sid=late.stat_ID AND late.statTable=early.statTable  $CRITERIA ;"

 

