#!/bin/sh  

if [ $# != 3 ] ; then
    echo 'Usage: coord_transform.sh infile outfilei zone '
    echo ''
    echo 'coordinate transformation from utm to ll'
    exit 1
fi

INFILE=$1
SPHEROID=clark66
UTMZONE=$3
OUTFILE=$2
touch $OUTFILE
rm $OUTFILE


    
GISBASE=/usr/local/grass5
GISDBASE=/home/farrell/gis
LOCATION_NAME=test
# MAKE SURE THAT THIS VARIABLE IS SET TO THE HOME OF THE USER
# RUNNING THE SERVLET
GISRC=/home/farrell/.grassrc5
GIS_LOCK=81198

export GISBASE
export LOCATION_NAME
echo $LOCATION_NAME
export GISDBASE
export GISRC
export GIS_LOCK


/usr/local/grass5/bin/m.u2ll -d  spheroid=$SPHEROID zone=$UTMZONE input=$INFILE output=$OUTFILE

	
cat $OUTFILE


 
