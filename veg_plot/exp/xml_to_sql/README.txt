#
#     '$Author: harris $'
#     '$Date: 2002-06-17 17:59:44 $'
#     '$Revision: 1.2 $'


vegclass-torque.sh is a shell script that transforms XML from our native format
   into a sql file on which we can build a database.

   Parameters are either plot, plant-taxonomy or comm-taxonomy to build one of those three modules.
   There is no default.

   First, it accesses a java file that converts XML into XML that torque likes.
   (Java file : VegclassXMLDoc)

   The resulting XML file (test_fix.xml) is copied to the torque directory.
  
   Then, Ant is envoked (with parameter project-sql)
   and it builds the SQL file from the torque XML.

   SQL file is copied to plot.sql, or plant-taxonomy.sql or comm-taxonomy.sql

   We are using torque-020302.tar.gz 02-Mar-2002 09:44  2.1M  GZIP compressed file and a 
	 version of this exists in the lib directory.

   3:18 PM 6/14/02 M. Lee
