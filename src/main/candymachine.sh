#!/bin/bash
#
### BEGIN INIT INFO
# Provides:             candymachine
# Required-Start:       $network
# Required-Stop:        $network
# Default-Start:        2 3 4 5
# Default-Stop:         0 1 6
# Short-Description:    Candy Machine
# Description:          Starts CandyMachine service
### END INIT INFO
# chkconfig: 2345 95 20

start() {
  sudo java -jar /home/pi/candies-client.jar iot.eclipse.org jeffprestes/candies/paulista whitesnow 1883 >> /home/pi/logcandies.log
}

stop() {
  echo 'CandyMachine was stopped' > /home/pi/logcandies.log
}

restart() {
  stop
  start
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    restart
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart}"
    exit 1
esac

exit $?