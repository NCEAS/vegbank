#!/bin/sh

# This is a hack to get the jars into the right place to allow project to build
# directly  from a cvs checkout
# I want subprojects to either to:
#           search for jars in a common place
#           or
#           all depend jar checked into there library  directory
# Not sure what approach is best now

# Add jars to databaseaccess library
cp  exp/data-translators/lib/datatranslator.jar  exp/database_access/lib/.
cp  exp/servlet/lib/utilities.jar exp/database_access/lib/.
cp  ../veg_taxa/lib/planttaxonomy.jar exp/database_access/lib/.

# Add jars to rmidatasource library
cp  exp/database_access/lib/database_access.jar exp/rmidatasource/lib/.
cp  exp/xml-resource/lib/xmlresource.jar exp/rmidatasource/lib/.

# Add jars to servlet library
cp  exp/xml-resource/lib/xmlresource.jar exp/servlet/lib/.
cp  ../veg_taxa/lib/planttaxonomy.jar exp/servlet/lib/.
cp  ../veg_community/lib/vegcommunity.jar exp/servlet/lib/.
cp exp/rmidatasource/lib/rmidatasource.jar exp/servlet/lib/.
cp  exp/database_access/lib/database_access.jar exp/servlet/lib/.
