#!/usr/bin/perl

$dbname=$ARGV[0];
$username=$ARGV[1];

if ( $dbname eq "" || $username eq "" )
{
	print "\n####################################################################\n";
	print "usage: $0 dbName userName\n";
	print "\tBoth parameters must be set\n";
	print "\tdbName: Database target for move of framework data e.g. vegbank.\n";
	print "\tuserName: The database user name. e.g. datauser.\n\n";
	print "\tFramework is assumed to be the database you pull data from.\n";
	print "\n####################################################################\n";
	exit();
}

print "\n######################################################################\n";
print "# Dump data from framework db.\n"; 
print `pg_dump -U $username framework > frameworkProd.sql`;

print "\n######################################################################\n";
print "# Create a frameworktest database for framework data\n"; 
;
print `createdb  -U $username frameworktest`;

print "\n######################################################################\n";
print "# Populate frameworktest\n"; 

print `psql  -U $username frameworktest < frameworkProd.sql`;

print "\n######################################################################\n";
print "# Modify frameworktest to fit new schema\n"; 

print `psql  -U $username frameworktest < src/sql/throwAways/prepareFrameworkForDumpToVB_1.02.sql`;

print "\n######################################################################\n";
print "# Dump frameworktest \n"; 

print `pg_dump -aD --disable-triggers  -U $username frameworktest > readyForImport.sql`;

print "\n######################################################################\n";
print "# Attempt to load frameworktest dump into production after running backup.\n"; 

print `pg_dump  -U $username $dbname > productionDump.bak`;
print `psql  -U $username $dbname < readyForImport.sql`;

print "\n######################################################################\n";
print "# Need to fix the party_id on usr table\n"; 

print `psql  -U $username $dbname --command "UPDATE usr SET party_id = (SELECT min(party_id) FROM party WHERE usr.email_address = party.email);"`;

print "\n######################################################################\n";
print "#CLEAN UP\n";
print `dropdb -U $username frameworktest`;

print "\n######################################################################\n";
print "\n\nThe process finishing running. Some temp files are left behind, \n";
print "frameworkProd.sql and readyForImport.sql These may be useful for debuging\n";
print "if anything went wrong.\n";

print "\nThere is a backup of the target database called productionDump.bak\n";
print "\n######################################################################\n";
