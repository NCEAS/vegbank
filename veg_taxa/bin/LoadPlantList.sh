#! /bin/sh

java -Xmx100M  -cp ../lib/jdbc7.0-1.2.jar:build/lib/planttaxonomy.jar:../lib/vegclass_common.jar:../lib/utils.jar org.vegbank.plants.datasource.LoadPlantList $1
