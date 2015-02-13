# This is the init script for starting up the
#  Jakarta Tomcat server
#
# chkconfig: 345 91 10
# description: Starts and stops the Tomcat daemon.
#

# Source function library.
. /etc/rc.d/init.d/functions

# Get config.
. /etc/sysconfig/network

# Check that networking is up.
[ "${NETWORKING}" = "no" ] && exit 0

tomcat=/srv/solr/instances/sc2geeks.v2
startup=$tomcat/start.sh
shutdown=$tomcat/stop.sh
export JAVA_HOME="/opt/app/java/current"

start(){
 echo -n $"Starting Tomcat service: "
 #daemon -c
 $startup
 RETVAL=$?
 # warm up request 
 # wget -q -a /dev/null http://localhost:6806/master/select/?q=*%3A*&version=2.2&start=0&rows=10&indent=on
 echo

}

stop(){
 action $"Stopping Tomcat service: " $shutdown 
 RETVAL=$?
 echo
}

restart(){
  stop
  sleep 2
  start
}


# See how we were called.
case "$1" in
start)
 start
 ;;
stop)
 stop
 ;;
status)
      # This doesn't work ;)
 status tomcat
 ;;
restart)
 restart
 ;;
*)
 echo $"Usage: $0 {start|stop|status|restart}"
 exit 1
esac

exit 0

