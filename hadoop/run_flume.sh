#!/bin/bash

if [ $# -ne 1 ]; then
        echo wrong number of parameters \(need one\)
        exit 0
fi

hadoop fs -rm -r -skipTrash /user/root/data/* 

mv -v data/input/example.log.COMPLETED data/input/example.log

# Launch Flume agent
flume-ng agent -n agent_$1 -c conf -f conf/flume_$1.conf -Dflume.root.logger=INFO,console

echo
echo ACCEPTED :
hadoop fs -ls -R /user/root/data/ACCEPTED
echo
echo REJECTED :
hadoop fs -ls -R /user/root/data/REJECTED
echo

# Launch Hive
hive -f hive/data_ct.hql
hive -e 'select count(*) from data ;
	select * from data limit 5 ; '
