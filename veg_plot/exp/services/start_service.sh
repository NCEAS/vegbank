#!/bin/sh -v


java -classpath lib/jsse.jar:lib/jnet.jar:/lib/jcert.jar:lib/dom.jar:lib/collections.jar:../../../lib/jdbc7.0-1.2.jar:../../../lib/servlet.jar:lib/GLUE-STD.jar:lib/vegservice.jar:build/WEB-INF/classes vegbank.publish.Publish
