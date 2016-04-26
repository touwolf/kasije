#!/bin/sh

case $1 in
        start)
            nohup java -server \
                       -Xms265m \
                       -Xmx3000m \
                       -Dcom.sun.management.jmxremote.port=59006 \
                       -Dcom.sun.management.jmxremote.authenticate=false \
                       -Dcom.sun.management.jmxremote.ssl=false \
                       -jar ./lib/kasije-main.jar start >> ./logs/start.log 2>&1 &
        ;;
        stop)
             java -jar ./lib/kasije-main.jar stop 59006
        ;;
esac
