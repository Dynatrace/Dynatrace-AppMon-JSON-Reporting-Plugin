#!/bin/sh

# keep this at Java 7 as it needs to run in a JVM 7 on the Dynatrace Collector!
#export JAVA_HOME=/devtools/jdk1.7.0_76_x64
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export PATH=$JAVA_HOME/bin:$PATH

export VERSION=6.5.0.1000

java -version

./gradlew -PdynaTraceVersion=$VERSION check plugin
exit $?
