#!/bin/bash

if [ $# -ne 1 ]; then
        echo wrong number of parameters \(need one\)
        exit 0
fi

hadoop fs -rm -r -skipTrash /user/root/data/*
rm -rfv data/REJECTED/* 

mv -v data/input/example.log.COMPLETED data/input/example.log

if [ $1 -ge 5 ]; then
	cd flume-serialization && mvn package
	mv -vf target/flume-event-serialization-1.0-SNAPSHOT.jar /usr/hdp/2.2.0.0-2041/flume/lib/
	cd ..
fi

# Launch Flume agent
if [ $1 -ge 15 ]; then
	flume-ng agent -n agent_$1_2 -c conf -f conf/flume_$1.conf -Dflume.root.logger=INFO,console & \
	flume-ng agent -n agent_$1_1 -c conf -f conf/flume_$1.conf -Dflume.root.logger=INFO,console
else
	flume-ng agent -n agent_$1 -c conf -f conf/flume_$1.conf -Dflume.root.logger=INFO,console
fi

echo
hadoop fs -ls -R /user/root/data/
echo
hadoop fs -cat /user/root/data/ACCEPTED/*
echo
ls -lh data/REJECTED/
echo

# Launch Hive
hive -f hive/data_ct.hql
hive -e 'select count(*) from data ;
	select * from data limit 5 ; '

