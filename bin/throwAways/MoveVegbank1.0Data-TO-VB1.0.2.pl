#!/usr/bin/perl

$dbname=$ARGV[0];
$username=$ARGV[1];

if ( $dbname eq "" || $username eq "" )
{
	print "\n####################################################################\n";
	print "usage: $0 dbName userName\n";
	print "\tBoth parameters must be set\n";
	print "\tdbName: Database target for move of vegbank-1.0 data e.g. vegbank. This dbName will be used to create the new db \n";
	print "\tuserName: The database user name. e.g. datauser.\n\n";
	print "\tFramework will be used to pull user data from.\n\n";
	print "NOTE: Must be run from the vegbank dir to pick up sql files etc.\n";
	print "\n####################################################################\n";
	exit();
}

print "\n######################################################################\n";
print "# Dump data from old db.\n"; 
print `pg_dump -U $username $dbName > oldProduction_1.0.1.bak`;

print "\n######################################################################\n";
print "# Create a temp database for old $dbName data\n"; 
print `createdb  -U $username ProductionTemp`;

print "\n######################################################################\n";
print "# Restore old 1.0.1 into ProductionTemp \n"; 
print `pg_restore -U $username -d $dbName oldProduction_1.0.1.bak`;

print "\n######################################################################\n";
print "# Modify ProductionTemp to fit new schema\n"; 
print `psql  -U $username ProductionTemp < src/sql/vegbank-changes-1.0.1to1.0.2_pg.sql`;

print "\n######################################################################\n";
print "# Dump ProductionTemp \n"; 
print `pgdump -aD  -U $username --disable-triggers ProductionTemp > vegbank101_102.sql`;

print "\n######################################################################\n";
print "# Drop $dbName\n"; 
print `dbdrop -U $username $dbName`;

print "\n######################################################################\n";
print "# Create $dbName\n"; 
print `dbcreate -U $username $dbName`;

print "\n######################################################################\n";
print "# Create empty $dbName using freshly generate SQL\n";
pring `echo vegbank | ant  db_generate_sql`;
print `psql -U $username $dbName < build/src/sql/vegbank-ML.sql`;

print "\n######################################################################\n";
print "# Create temp fields to drop latter\n"; 
print `psql -U $username $dbName src/sql/vegbank-changes-1.0.1to1.0.2_pg_createfields2drop.sql`;

print "\n######################################################################\n";
print "# Populate with modified 1.0.1 to 1.0.2 data\n"; 
print `pg_restore  -U $username -d $dbName vegbank101_102.sql`;

print "\n######################################################################\n";
print "# Drop temp fields\n"; 
print `psql -U $username $dbName < src/sql/vegbank-changes-1.0.1to1.0.2_pg_drop.sql`;

print "\n######################################################################\n";
print "# Attempt to load modified (1.0.2) production (1.0.1)  dump into $dbName.\n"; 
print `psql  -U $username $dbName < vegbank101_102.sql`;

# Need to run post processing
# Run Framework to Vegbank
# Run Throwaways

print "\n######################################################################\n";
print "#CLEAN UP\n";
print `dropdb -U $username ProductionTemp`;

print "\n######################################################################\n";
print "\n\nThe process finishing running. Some temp files are left behind, \n";
print "oldProduction_1.0.1.bak and  vegbank101_102.sq These may be useful for debuging\n";
print "if anything went wrong.\n";

print "\nThere is a backup of the target database called productionDump.bak\n";
print "\n######################################################################\n";
