#!/bin/sh
. setclasspath
java -cp lib/vegservice.jar:${CP} vegbank.publish.VegServiceInvoker $@ 
