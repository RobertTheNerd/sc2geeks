#!/usr/bin/env bash

scriptpath=$0
case $scriptpath in
 ./*)  SCRIPT_PATH=`pwd` ;;
  * )  SCRIPT_PATH=`dirname $scriptpath`
esac


#CATALINA_BASE=$SCRIPT_PATH/tomcat
export CATALINA_BASE

if [ -d $SCRIPT_PATH/bin ]; then
echo Stopping Tomcat
$SCRIPT_PATH/bin/shutdown.sh
else
   echo "Tomcat Path Error"
fi

