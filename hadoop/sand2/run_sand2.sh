#!/bin/bash

hadoop fs -rm -r /user/root/data/* 

mv -v data/input/example.log.COMPLETED data/input/example.log

# Launch Flume agent
flume-ng agent -n sand2 -c conf -f sand2/flume_2.conf -Dflume.root.logger=INFO,console

echo
echo ACCEPTED :
hadoop fs -ls -R /user/root/data/ACCEPTED
echo

