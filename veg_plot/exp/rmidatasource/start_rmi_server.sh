#!/bin/sh

java -classpath ./build/:lib/rmidatasource.jar:lib/datatranslator.jar:./  -Djava.security.policy=./lib/policy.txt DataSourceServer
