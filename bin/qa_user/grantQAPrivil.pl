#!/usr/bin/perl

#List of databases to visit
@databases =  ('framework','vegbank'); 

foreach $database (@databases) {

  # Get the list of tables in database
  print "#################\n# Get tables for $database\n##################\n";
  @tables = `psql $database -t -c  "select tablename from pg_tables"`;

  foreach $table (@tables) {
    $table =~ m/^\s*$/ and next;
    $table =~ m/^\s*pg_/ and next;
    # run a grant on this table
    print "Run grant on $table";
    `psql $database -c "GRANT SELECT ON $table TO qa"`;
                        
  }
}
