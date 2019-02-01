#!/bin/sh

echo "Compile tests"
mvn clean install -U
echo "Execute tests"
cd target/
java -cp DemoSwagger-1.0-SNAPSHOT-jar-with-dependencies.jar  org.haitham.demoswagger.TestExecutor

echo "Test results reports are available at: output directolry"
ls -la output/

echo
exit
