#!/usr/bin/env bash

scriptpath=$0
case $scriptpath in
 ./* )  SCRIPT_PATH=`pwd` ;;
  * )  SCRIPT_PATH=`dirname $scriptpath`
esac

SOLR_HOME=$SCRIPT_PATH/solr
case "`uname`" in
  CYGWIN*) SOLR_HOME=`cygpath -w $SOLR_HOME`
  ;;
esac

SERVER="-server"
HEAPSIZE="-Xms512M -Xmx1G"

JVM=java
#does the jvm support -server?
$JVM -server -version > /dev/null 2>&1
if [ $? != "0" ]; then
  SERVER=""
fi

#CATALINA_BASE=$SCRIPT_PATH/tomcat
#CATALINA_LOG=$SCRIPT_PATH/logs/tomcat
HOSTNAME=$(hostname)
JAVA_OPTS="$SERVER $HEAPSIZE -Dsolr.solr.home=$SOLR_HOME -Dorg.apache.lucene.FSDirectory.class=org.apache.lucene.store.NIOFSDirectory -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8510 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dsubject=[$HOSTNAME]#5611_WebsiteFeature_Master"
export JAVA_OPTS #CATALINA_BASE CATALINA_LOG

echo Solr home: $SOLR_HOME
## start tomcat
if [ -d $SCRIPT_PATH/bin ];
##tomcat
then
 echo "Starting Tomcat"
 $SCRIPT_PATH/bin/catalina.sh start
else
 echo "Tomcat Path Error"
fi
