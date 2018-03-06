#!/bin/bash
###################################################
#Usage: ./jkx-getway {start|stop|restart|status}
###################################################
#set -e
PROG=jkx-getway-server
CMD_PRG="$0"
PRGDIR=`dirname "$CMD_PRG"`
PRO_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
LOG_HOME=$PRO_HOME/logs
echo "[Set 1] install dir : $PRO_HOME"
if [ -x "$PRO_HOME"/"bin/env.sh" ];then
	echo "[Set 1] setting source $PRO_HOME/bin/env.sh"
	source $PRO_HOME/bin/env.sh ||exit
fi


if [ "$JAVA_HOME" != "" ]; then
  JAVA_HOME=$JAVA_HOME
fi

if [ "$JAVA_HOME" = "" ]; then
  echo "WARN: JAVA_HOME is not set."
 # exit 1
fi


PIDFILE=$PRO_HOME/pid


status() {
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        #验证进程号是否真的在系统的存在  暂时 不验证
    # ps_PIDLIST=`ps -ef|grep $CPS_PID|grep -v grep|awk -F" " '{print $2}'`
    if [ ! -x /proc/${PID} ]; then
            return 1
        else
            return 0
        fi
    else
        return 1
    fi
}
case "$1" in
    start)
        status
        RETVAL=$?
	if [ $RETVAL -eq 0 ]; then

            echo "$PIDFILE exists, process is already running($(cat $PIDFILE))"
            exit 1
        fi

        FILES=`ls $PRO_HOME/lib`
        echo $FILES

        for jarname in $FILES
        do
                JARS=$PRO_HOME/lib/$jarname
        done
        execmd=java
        if [ "$JAVA_HOME" != "" ]; then
           execmd=$JAVA_HOME/bin/java
        fi
        echo "Starting $execmd  $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS     -Dsoft.home=$PRO_HOME/  -Dsoft.logs=$LOG_HOME  -jar $JARS   --spring.config.location=file:$PRO_HOME/conf/  > /dev/null 2>&1 &"
            nohup $execmd  $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS   -Dsoft.home=$PRO_HOME/  -Dsoft.logs=$LOG_HOME   -jar $JARS   --spring.config.location=file:$PRO_HOME/conf/  > /dev/null 2>&1 &
            #exec $execmd  -jar $JARS   $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS --spring.config.location=file:$PRO_HOME/conf   &
            RETVAL=$?
            if [ $RETVAL -eq 0 ]; then
                _pid=$!
                echo "$PROG is started($_pid)"
                echo $_pid > $PIDFILE
                exit 0
            else
                echo "Stopping $PROG"
                rm -f $PIDFILE
                exit 1
            fi
            ;;
    stop)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "Shutting down $PROG `cat $PIDFILE`"
            kill `cat $PIDFILE`
            RETVAL=$?
            if [ $RETVAL -eq 0 ]; then
                rm -f $PIDFILE
            else
                echo "Failed to stopping $PROG"
            fi
        else
            echo 'not running'
        fi
        ;;
    status)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            PID=$(cat $PIDFILE)
            echo "$PROG is running ($PID)"
        else
            echo "$PROG is not running"
        fi
        ;;
    watch)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
           tail -f $2 |

            while IFS= read line
              do
                echo "$msgBuffer" "$line"

                if [[ "$line" == *"Started "* ]]; then
                    echo Application Started... exiting buffer!
                    pkill  tail
                fi
            done
        else
            echo "$PROG is not running"
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        ;;
esac

