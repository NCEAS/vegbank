#!/usr/bin/perl -w
# Name: CompareInputToOutput.pl
# '$Author: farrell $'
# '$Date: 2002-12-27 21:27:38 $'
# '$Revision: 1.1 $'
#
# Description:
#   Compare the input data (xml) with a sql dump of that data.

use Data::Dumper;
use strict;

# Set some magic variables to get behavior I want
################################################
$SIG{__WARN__} = sub { }; # Allow Deep Recursion
undef $/;  # Slurp Mode
$| = 1;    # No Buffering
################################################

my %inputXmlNameValueHash;
my %outNameValueHash;

# Read in input xml file and create a hash from it 
my $inputFile = "../TestData/nativeDataSource.xml";
open INPUTFILE, $inputFile or die "Could not open $inputFile : $!";
my $text = <INPUTFILE>;
close INPUTFILE;

# Read in input xml file and create a hash from it 
my $outputFile = "/usr/local/devtools/jakarta-tomcat/webapps/framework/WEB-INF/lib/atomicResult";
open OUTFILE, $outputFile or die "Could not open $outputFile : $!";
my $outText = <OUTFILE>;
close OUTFILE;

# Read in results file and create a hash from it 
my $resultsFile = "lastPlotinBD.txt";

findElement($text, 'in');
findElement($outText, 'out');
my %fieldNameValueHash = parseSQLOutput($resultsFile);

#print Dumper(%inputXmlNameValueHash);
#print Dumper(%fieldNameValueHash);
#print "\n representativeness = $inputXmlNameValueHash{'representativeness'}\n";
#print "\n notespublic = $fieldNameValueHash{'notespublic'}\n";
compare(\%inputXmlNameValueHash, \%outNameValueHash);
#compare(\%inputXmlNameValueHash, \%fieldNameValueHash);
#################################################
# Subroutines
#################################################
sub findElement {
  my ($text, $mode) = @_;
  $text =~ s/<(\w+)>/handleElement($1, $', $mode)/ieo;
}

sub handleElement {
  my ($elementName, $restOfText, $mode) = @_;
  # Either has text of element as child
  $restOfText =~ m/^\s*</ and findElement($restOfText, $mode) and return "done"; # has element
  $restOfText =~ s/^\s*([^<]+)/handleValue($1, $elementName, $', $mode)/ieo;
}

sub handleValue {
  my ($elementValue, $elementName, $restOfText, $mode) = @_;
  #$elementValue = lc($elementValue);  # Ignore Case
  $elementName = lc($elementName);  # Ignore Case
  $mode eq 'in' and $inputXmlNameValueHash{$elementName} = $elementValue;
  $mode eq 'out' and $outNameValueHash{$elementName} = $elementValue;
  findElement($restOfText, $mode);
  return 0;
}

sub parseSQLOutput {
  my ($resultsFile) = @_;
  local ($/) = "\n";
  my (@names, @values, %fieldNameValueHash);
  open RESULTSFILE, $resultsFile or die "Could not open $resultsFile : $!";
  #while (<RESULTSFILE>) {
  for (my $i=0;$i<3;$i++) {
    $_ = <RESULTSFILE>;
    m/^-------------/ and next; # useless line throw away
    $i == 0 and @names = split(/\s*\|\s*/);
    $i == 2 and @values = split(/\s*\|\s*/);
  }
  close RESULTSFILE;
  #print "\n\nLast Name: $names[$#names] vs. Last Value: $values[$#values] ---> vl = $#values and nl =$#names";
  # create a hash
  for (my $i=0;$i<=$#names;$i++) {
    my $name = $names[$i];
    my $value = $values[$i];
    # clean whitespace
    $name = trim($name);
    $value = trim($value); 

    $fieldNameValueHash{$name} = $value;
    #print "... $name = $value\n";
  }
  return %fieldNameValueHash;
}

sub trim {
  my ($text) = @_;
  #print ">>>> $text";
  $text =~ s/^\s*(.*?)\s*$/$1/g; 
  #print ">>>> $text\n";
  return $text;
}

sub compare {
  my ($hashref, $hashref2) = @_;
  foreach (keys %$hashref) {
   
    if (exists $$hashref2{$_}) {
      print "$_ exists";

      if ($$hashref{$_} eq $$hashref2{$_}) {
        print " - GOOD\n";
      } else {
        print " #####################
        BUT is not the same value -- '$$hashref{$_}' vs. '$$hashref2{$_}' --\n";
      }
    } else {
      print "################# $_ not found in output\n";
    }
      

    # Name Exists or not 
    # Value is the same or not
  }

}

