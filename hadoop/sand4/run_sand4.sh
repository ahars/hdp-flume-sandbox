#!/bin/bash

hadoop fs -rm -r /user/root/data/* 

mv -v data/input/example.log.COMPLETED data/input/example.log

# Launch Flume agent
flume-ng agent -n sand4 -c conf -f sand4/flume_4.conf -Dflume.root.logger=INFO,console

echo
echo ACCEPTED :
hadoop fs -ls -R /user/root/data/ACCEPTED
echo

