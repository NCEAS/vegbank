#!/usr/bin/perl

# This is a utility to resync the sequence value to to the max pk value to 
# prevent the duplicate Primary Key value being returned from the sequence.

#List of databases to visit
@databases =  ('vegbank'); 

foreach $database (@databases) {

  # Get the list of tables in database
  print "#################\n# Get tables for $database\n##################\n";
  @tables = `psql $database -t -c  "select tablename from pg_tables"`;

  foreach $table (@tables) {
    $table =~ m/^\s*$/ and next; # Skip empty strings
    $table =~ m/^\s*pg_/ and next; # Skip internal tables
    $table =~ s/^\s*//; # Strip leading spaces
    chomp($table); # Strip trailing spaces
    print "\'$table\'\n";
	

    # run a grant on this table
    print "\nRun update Sequence on $table\n";
    $sql = "SELECT setval('$table" . "_" . "$table" . "_id_seq',MAX($table" . "_id)) FROM $table"; 
    print "psql $database -c \"$sql\"\n";
    print `psql $database -c \"$sql\"`;
  }
  # Theres always one that disobays the rule :)
  print `psql $database -c \"SELECT setval('aux_role_role_id_seq',MAX(role_id)) FROM aux_role\"`;
  # Make that two bastards
  print `psql $database -c \"SELECT setval('place_plotplace_id_seq',MAX(plotplace_id)) FROM place\"`;
}
