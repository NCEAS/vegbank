#!/bin/sh
#  A shell script that starts the ESA database 
#  client software on a UNIX machine
#
#*  '$Author: harris $'
#*  '$Date: 2001-10-12 18:00:53 $'
#* 	'$Revision: 1.1 $'


echo "starting the ESA Vegetation datbase installer"

java -classpath $CLASSPATH:Installer.jar vegclient.installer.InstallerInterface


