#!/bin/bash

if [ $# -ne 1 ]; then
        echo wrong number of parameters \(need one\)
        exit 0
fi

hadoop fs -rm -r /ACCEPTED 
hadoop fs -rm -r /REJECTED
hadoop fs -mkdir /ACCEPTED
hadoop fs -mkdir /REJECTED

mv -v data/input/example.log.COMPLETED data/input/example.log

# Launch Flume agent
flume-ng agent -n agent_$1 -c conf -f conf/flume_$1.conf -Dflume.root.logger=INFO,console

echo
echo ACCEPTED :
hadoop fs -ls /ACCEPTED
echo
echo REJECTED :
hadoop fs -ls /REJECTED
echo

