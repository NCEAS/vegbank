#!/bin/sh
#
# Shell script for launching the desktop data management client
#
# Author: J. Harris
# Date: 3/08/2001 


XMLP=../lib/xerces.jar
XALAN=../lib/xalan.jar
VCLIENT=../lib/interface.jar
CPATH=.:$XMLP:$XALAN:$VCLIENT
java   -cp $CPATH -jar $VCLIENT
