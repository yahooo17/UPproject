rmdir -rf bin

mkdir bin

"%JAVA_HOME%/bin/javac" -d bin -sourcepath src -cp libs/json-simple-1.1.1.jar;libs/javax.json-api-1.0.jar;libs/javax.json-1.0.4.jar src/by/bsu/
up/chat/*.java

