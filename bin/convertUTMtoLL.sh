#!/bin/sh
#
# $Id: convertUTMtoLL.sh,v 1.1 2004-10-20 00:48:09 anderson Exp $
#
# Converts UTM to Lat/Long coords, given a file like this:
# YOSE.99K27,241939,4205221,11
# ...producing inputfile.ll like this:
# YOSE.99K27,37.957958,119.93729
#  output is ID NS EW
# 

rm -f $1.ll

cat $1 | while read line; 
do 
	tmpLine=`echo $line | cut -d , -f 2- | sed "s/,/ /g"`;
	y=`echo $tmpLine | awk '{print $1}'`; 
	x=`echo $tmpLine | awk '{print $2}'`; 
	zone=`echo $tmpLine | awk '{print $3}'`; 

	id=`echo $line | cut -d , -f 1`;
	llCoords=`./utm2ll $x $y $zone`
	NShemi=`echo $llCoords | awk '{print $1}'`; 
	NS=`echo $llCoords | awk '{print $2}'`; 
	EWhemi=`echo $llCoords | awk '{print $3}'`; 
	EW=`echo $llCoords | awk '{print $4}'`; 

	if (test $NShemi == 'S') then
		NS="-$NS"
	fi
	if (test $EWhemi == 'E') then
		EW="-$EW"
	fi

	echo "$id,$NS,$EW" >> $1.ll
done
