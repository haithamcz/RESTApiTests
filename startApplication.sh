#!/bin/sh

$JAVA_HOME/bin/java -Djsse.enableSNIExtension=false -jar ./lib/trade-validation-service-1.0.0-SNAPSHOT.jar
