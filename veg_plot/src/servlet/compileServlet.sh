#!/bin/sh

javac -classpath /usr/local/devtools/jakarta-tomcat/lib/servlet.jar:./base.jar:./plotAccess.jar:./ $1


#touch plantTrax.jar
#rm plantTrax.jar

#jar -cvf plantTrax.jar plantTrax*
