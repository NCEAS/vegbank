#! /bin/sh

java  -cp ../lib/devpgjdbc2.jar:build/lib/planttaxonomy.jar:../lib/vegclass_common.jar:../lib/utils.jar org.vegbank.plants.datasource.LoadPlantList $1
