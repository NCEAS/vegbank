#!/usr/bin/perl

my $finalDestinationDB;
my $dataSourceDB;
my $tempDB = "ProductionTemp";


$finalDestinationDB=$ARGV[0];
$username=$ARGV[1];
$dataSourceDB = $ARGV[2];
if ( ! $ARGV[3] eq "" )
  {
    $tempDB = $ARGV[3];
  }

if ( $finalDestinationDB eq "" || $username eq "" || $dataSourceDB eq "" )
{
	print "\n####################################################################\n";
	print "usage: $0 [finalDestinationDB] [userName] [dataSourceDB] [tempDB]\n";
	print "\t First three parameters must be set\n";
	print "\t finalDestinationDB: Database target for move of vegbank-1.0.1 data e.g. vegbank.\n";
	print "\t dataSourceDB: The 1.0.1 DB to pull data from \n";
	print "\t userName: The database user name. e.g. datauser.\n\n";
	print "\t Framework will be used to pull user data from.\n\n";
	print "NOTE: Must be run from the vegbank dir to pick up sql files etc.\n";
	print "\n####################################################################\n";
	exit();
}

print "\n######################################################################\n";
print "# Dump data from old db into oldProduction_1.0.1.bak.\n"; 
print `pg_dump $dataSourceDB -U $username > oldProduction_1.0.1.bak`;

print "\n######################################################################\n";
print "# Create a temp database for old $dataSourceDB data, drop existing first\n"; 
print `dropdb -U $username $tempDB`;
print `createdb  -U $username -E UNICODE $tempDB`;

print "\n######################################################################\n";
print "# Convert the file to UTF-8\n"; 
print `iconv  -f ISO-8859-1 -t UTF-8 oldProduction_1.0.1.bak -o oldProduction_1.0.1-UTF-8.bak `;


print "\n######################################################################\n";
print "# Restore old 1.0.1 into $tempDB \n"; 
print `psql -U $username  $tempDB < oldProduction_1.0.1-UTF-8.bak`;

print "\n######################################################################\n";
print "# ReSync keys and Pks tempDB ... and SLEEP until finished \n"; 
print `./bin/ReSyncPKsWithSeqence.pl $tempDB`;

print "\n######################################################################\n";
print "# Modify $tempDB to fit new schema\n"; 
print `psql  -U $username $tempDB < src/sql/vegbank-changes-1.0.1to1.0.2_pg.sql`;

print "\n######################################################################\n";
print "# Dump $tempDB \n"; 
print `pg_dump -aD --disable-triggers $tempDB > vegbank101_102.sql`;

print "\n######################################################################\n";
print "# Strip sequence setting from dump \n";
stripOutSetSequence( "vegbank101_102.sql", "vegbank101_102-WITHOUT_SEQUENCE.sql"  );

# No need for this since read from UNICODE db? -- seems to work
#print "\n######################################################################\n";
#print "# Convert the file to UTF-8\n"; 
#print `iconv  -f ISO-8859-1 -t UTF-8 vegbank101_102.sql  -o vegbank101_102-UTF-8.sql `;

print "\n######################################################################\n";
print "# Drop $finalDestinationDB\n";
print `dropdb -U $username  $finalDestinationDB`;

print "\n######################################################################\n";
print "# Create $finalDestinationDB\n"; 
print `createdb -U $username -E 'UNICODE' $finalDestinationDB`;

print "\n######################################################################\n";
print "# Generate fresh SQL\n";
print `echo vegbank | ant  db_generate_sql`;

print "\n######################################################################\n";
print "# Create empty $finalDestinationDB using freshly generate SQL\n";
print `psql -U $username $finalDestinationDB < build/src/sql/vegbank-ML.sql`;

print "\n######################################################################\n";
print "# Create temp fields on $finalDestinationDB to drop latter\n"; 
print `psql -U $username $finalDestinationDB < src/sql/vegbank-changes-1.0.1to1.0.2_pg_createfields2drop.sql`;

print "\n######################################################################\n";
print "# Attempt to load modified (1.0.2) production (1.0.1)  dump into $finalDestinationDB.\n"; 
print `psql  -U $username  $finalDestinationDB  < vegbank101_102-WITHOUT_SEQUENCE.sql`;

print "\n######################################################################\n";
print "# ReSync sequences and Pks on $finalDestinationDB ... and SLEEP until finished \n"; 
print `./bin/ReSyncPKsWithSeqence.pl $tempDB`;


print "\n######################################################################\n";
print "# Drop temp fields in $finalDestinationDB\n"; 
print `psql -U $username $finalDestinationDB < src/sql/vegbank-changes-1.0.1to1.0.2_pg_drop.sql`;

# Need to run post processing
# Run Framework to Vegbank
# Run Throwaways
# ReSyncSequences and PKs

print "\n######################################################################\n";
print "#CLEAN UP\n";
#print `dropdb -U $username $tempDB`;

print "\n######################################################################\n";
print "\n\nThe process finishing running. Some temp files are left behind, \n";
print "oldProduction_1.0.1.bak and  vegbank101_102.sq These may be useful for debuging\n";
print "if anything went wrong.\n";

print "\nThere is a backup of the target database called productionDump.bak\n";
print "\n######################################################################\n";



sub stripOutSetSequence
  {
    my ($InFileName, $outFileName) = @_;
    open(INPUT, "$InFileName");
    open(OUTPUT, ">$outFileName");

    while (<INPUT> )
      {
	my ($line) = $_; #preserve $_, the line just read, nomatter what
	chomp($line); #blow off trailing newline

	if ( $line =~ /pg_catalog\.setval/i)
	  {
	    print OUTPUT "-- $line\n";
	  }
	else
	  {
	    print OUTPUT "$line\n";
	  }
      }
    close(INPUT);
    close(OUTPUT);
  }
