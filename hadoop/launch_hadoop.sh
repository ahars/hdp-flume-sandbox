#!/bin/bash

# Stop HDFS namenode & datanode
/usr/hdp/current/hadoop-hdfs-datanode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR stop datanode
/usr/hdp/current/hadoop-hdfs-namenode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR stop namenode

# Launch HDFS namenode & datanode
/usr/hdp/current/hadoop-hdfs-namenode/../hadoop/bin/hdfs namenode -format
/usr/hdp/current/hadoop-hdfs-namenode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR start namenode
/usr/hdp/current/hadoop-hdfs-datanode/../hadoop/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR start datanode

