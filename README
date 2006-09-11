#
# '$RCSfile: README,v $'
# '$Author: mlee $'
# '$Date: 2006-09-11 22:03:55 $'
# '$Revision: 1.18 $'
# '$Date: 2006-09-11 22:03:55 $'
#

VEGBANK: Vegatation Plots Database and Archival System
------------------------------

Version: @version@

Send feedback and bugs to: vegbank-dev@ecoinformatics.org
                           http://bugzilla.ecoinformatics.org

Contributors: Robert K. Peet
              Michael D. Jennings
              Dennis Grossman
              Marilyn D. Walker
              Mark Anderson (anderson@nceas.ucsb.edu) 
              Gabriel Farrell (farrell@nceas.ucsb.edu)
              John Harris (harris@nceas.ucsb.edu)
              Micheal Lee (mikelee@email.unc.edu)
              Chad Berkley (berkley@nceas.ucsb.edu)
              
VegBank is the vegetation plot database of the Ecological Society of America's 
Panel on Vegetation Classification. VegBank consists of three modules that 
contain (1) the actual plot records, (2) vegetation types recognized in the U.S. 
National Vegetation Classification and other vegetation types submitted by users, 
and (3) all plant taxa recognized by ITIS/USDA as well as all other plant taxa 
recorded in plot records. Vegetation records, community types and plant taxa may 
be submitted to VegBank and may be subsequently searched, viewed, annotated, revised, interpreted, downloaded, and cited.

Installation
------------
See INSTALL file in this directory.


PROPERTIES FILES:
----------------
./build.properties - edit for your environment

DATABASE SETUP:
---------------
** For more information, see the INSTALL file.

Vegbank has a single database for Plots, Plant Taxonomy and Plant 
Community.  It also includes information about users of the VegBank system
and their personal datasets.

Initializing PostgresSQL:

Install PostgresSQL on the system ... 

# Associate a file with the database ( e.g /home/username/pgdata )
initdb [databasefile]

# Start database ( with debuging output )
postmaster -d2 -i -D databasefile 

# Create the database users as follows:
In an account that has rights to the database ( e.g. postgres )
> createuser  datauser
> createuser [ your unix username ]
> createuser qa

# Build the databases
Some of the sql is auto generated using an ant task.
In the vegclass directory run:
> ant createdb

# Edit the pg_hba.conf file so that connections may be made to the database 
# add the following:
local      all                                               trust
host       all  127.0.0.1                 255.255.255.255    trust
#host       all  ip of RMI server etc.    255.255.255.255    trust


Major Known Bugs or Feature Requests (see http://bugzilla.ecoinformatics.org)
-----------------------------------------------------------------------------
If you discover a bug
please report it, either by email (above) or by using our bug tracking
system (http://bugzilla.ecoinformatics.org). There is a list of currently
unimplemented features in Bugzilla that we are working on for the next
release.

Legalese
--------
This software is copyrighted by The Regents of the University of California
and the National Center for Ecological Analysis and Synthesis
and licensed under the GNU GPL; see the 'LICENSE' file for
details.

This material is based upon work supported by the National Science Foundation,
Grant Nos. DBI-9905838 and DBI-0213794.
Any opinions, findings and conclusions or recomendations expressed in this
material are those of the author(s) and do not necessarily reflect
the views of the National Science Foundation (NSF).

This product includes software developed by the Apache Software
Foundation (http://www.apache.org/). See lib/apache-licence.txt
for details.

The Struts workflow extension is also included see 
lib/workflow-extension-licence.txt for details.
