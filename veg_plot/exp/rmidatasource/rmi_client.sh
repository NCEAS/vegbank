HOST=$1
FILE=$2

java -classpath ./lib/datatranslator.jar:./:./lib/rmidatasource.jar  \
-Djava.security.policy=./lib/policy.txt DataSourceClient $2 $1 
