#!/bin/bash

if [ $# -ne 1 ]; then
        echo wrong number of parameters \(need one\)
        exit 0
fi

# Launch HDFS namenode & datanode
/usr/hdp/current/hadoop-hdfs-namenode/../hadoop/bin/hdfs namenode -format
/usr/hdp/current/hadoop-hdfs-namenode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR start namenode
/usr/hdp/current/hadoop-hdfs-datanode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR start datanode

hadoop fs -mkdir /ACCEPTED
hadoop fs -mkdir /REJECTED

# Launch Flume agent
flume-ng agent -n agent_$1 -c conf -f ../conf/flume_$1.conf -Dflume.root.logger=INFO,console

