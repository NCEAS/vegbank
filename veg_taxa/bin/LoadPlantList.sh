#! /bin/sh

java -Xmx150M  -cp ../lib/jdbc7.0-1.2.jar:build/lib/planttaxonomy.jar:../lib/vegclass_common.jar:../lib/utils.jar:../lib/vegbank_datamodel.jar:../veg_plot/build/lib/database_access.jar org.vegbank.plants.datasource.LoadPlantList $1 $2 $3
