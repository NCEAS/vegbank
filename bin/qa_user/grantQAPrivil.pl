#!/usr/bin/perl

#List of databases to visit
@databases =  ('vegbank'); 

$user = $ARGV[0];
$permissions = $ARGV[1];

if ( $user eq "" )
{
  $user = "qa"
}

if ( $permissions eq "" )
{
  $permissions = "SELECT";
}

foreach $database (@databases) {

  # Get the list of tables in database
  print "#################\n# Get tables for $database\n##################\n";
  @tables = `psql -U datauser $database -t -c  "select tablename from pg_tables"`;

  foreach $table (@tables) {
    $table =~ m/^\s*$/ and next;
    $table =~ m/^\s*pg_/ and next;
    $table =~ s/^ (.*)/$1/;
    chomp($table);

    # run a grant on this table
    print "Run grant on $table\n";
    `psql -U datauser $database -c "GRANT $permissions ON $table TO $user"`;
     
     $sequence = $table . "_" . $table . "_id_seq \n";
     print "Run grant on $sequence";
    `psql -U datauser $database -c "GRANT $permissions ON $sequence TO $user"`;
                        
  }
}
