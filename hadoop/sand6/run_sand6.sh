#!/bin/bash

hadoop fs -rm -r /user/root/data/* 

mv -v data/input/example.log.COMPLETED data/input/example.log

cd sand5/flume-serializer-sand5 && mvn package
mv -vf target/flume-serializer-sand5-1.0-SNAPSHOT.jar /usr/hdp/2.2.0.0-2041/flume/lib/
cd ../..

# Launch Flume agent
flume-ng agent -n sand6 -c conf -f sand6/flume_6.conf -Dflume.root.logger=INFO,console

echo
echo ACCEPTED :
hadoop fs -ls -R /user/root/data/ACCEPTED
echo

