#!/bin/sh
#
# Tests build.properties for missing properties.
#

returnCode=0
for p in `cat build.properties.compare`; do 
	rm -f .build.properties.compare.tmp
	grep -l $p build.properties > .build.properties.compare.tmp

	if [ ! -s .build.properties.compare.tmp ]
	then
		echo MISSING PROPERTY:  $p
		returnCode=1
	fi
done

exit $returnCode
