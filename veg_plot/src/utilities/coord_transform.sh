#!/bin/sh  

if [ $# != 2 ] ; then
    echo 'Usage: coord_transform.sh infile outfile '
    echo ''
    echo 'coordinate transofmation from utm to ll'
    exit 1
fi

INFILE=$1
SPHEROID=clark66
UTMZONE=17
OUTFILE=$2
touch $OUTFILE
rm $OUTFILE


    
GISBASE=/home/harris/grass
GISDBASE=/home/harris/gis
GISBASE=/home/harris/grass
LOCATION_NAME=test
GISRC=/home/harris/.grassrc5
GIS_LOCK=81198

export GISBASE
export GISDBASE
export LOCATION_NAME
export GISRC
export GIS_LOCK


/home/harris/grass/bin/m.u2ll -d  spheroid=$SPHEROID zone=$UTMZONE input=$INFILE output=$OUTFILE

	
cat $OUTFILE


 
