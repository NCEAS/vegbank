#!/usr/bin/perl
# $Id: ReSyncPKsWithSeqence.pl,v 1.6 2004-02-27 01:52:41 anderson Exp $

# This is a utility to resync the sequence value to to the max pk value to 
# prevent the duplicate Primary Key value being returned from the sequence.
#
# WARNING!!  The max PK is used currently.  Might need to use greater of
# PK or current sequence value.

$numberOfArgs = @ARGV;
if ( $numberOfArgs <= 1 )
{
	print "\n####################################################################\n";
	print "\n Usage: \n";
	print "\t ReSyncPKsWithSeqence.pl [databaseName] [username]\n";
	print "\n####################################################################\n";
	exit();	
}
$database = $ARGV[0];
$username = $ARGV[1];

  # Get the list of tables in database
  print "#################\n# Get tables for $database\n##################\n";
  @tables = `psql -U $username $database -t -c  "select tablename from pg_tables"`;

  foreach $table (@tables) {
    $table =~ m/^\s*$/ and next; # Skip empty strings
    $table =~ m/^\s*pg_/ and next; # Skip internal tables
    $table =~ s/^\s*//; # Strip leading spaces
    chomp($table); # Strip trailing spaces
	

    print "\n########################### Sync $table ########################################\n";
    # run a grant on this table
    print "\nRun update Sequence on $table\n";
    $sql = "SELECT setval('$table" . "_" . "$table" . "_id_seq',MAX($table" . "_id)) FROM $table"; 
    print "psql -U $username $database -c \"$sql\"\n";
    print `psql -U $username $database -c \"$sql\"`;
    
    # Hack because older vegbanks sometimes use different naming convention
    $sql = "SELECT setval('$table" . "_id_seq',MAX($table" . "_id)) FROM $table"; 
    print "psql -U $username $database -c \"$sql\"\n";
    print `psql -U $username $database -c \"$sql\"`;
    
    print "\n########################### Sync $table complete  ########################################\n";
    }

  # Theres always one that disobays the rule :)
  print `psql -U $username $database -c \"SELECT setval('aux_role_role_id_seq',MAX(role_id)) FROM aux_role\"`;
  # Make that two bastards
  print `psql -U $username $database -c \"SELECT setval('place_plotplace_id_seq',MAX(plotplace_id)) FROM place\"`;

