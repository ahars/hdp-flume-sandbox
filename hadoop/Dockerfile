FROM centos:6
MAINTAINER Antoine Hars <antoine.hars@gmail.com>

# Environment variables for Hadoop
ENV JAVA_HOME /usr/java/latest
ENV HDFS_USER hdfs
ENV YARN_USER yarn
ENV MAPRED_USER mapred
ENV PIG_USER pig
ENV HIVE_USER hive
ENV WEBHCAT_USER hcat
ENV HBASE_USER hbase
ENV ZOOKEEPER_USER zookeeper
ENV OOZIE_USER oozie
ENV ACCUMULO_USER accumulo
ENV FALCON_USER falcon
ENV SQOOP_USER sqoop
ENV KAFKA_USER kafka
ENV STORM_USER storm
ENV KNOX_USER knox
ENV NAGIOS_USER nagios
ENV HADOOP_GROUP hadoop
ENV MAPRED_GROUP mapred
ENV NAGIOS_GROUP nagios
ENV DFS_NAME_DIR /grid/hadoop/hdfs/nn /grid1/hadoop/hdfs/nn
ENV DFS_DATA_DIR /grid/hadoop/hdfs/dn /grid1/hadoop/hdfs/dn /grid2/hadoop/hdfs/dn
ENV FS_CHECKPOINT_DIR /grid/hadoop/hdfs/snn /grid1/hadoop/hdfs/snn /grid2/hadoop/hdfs/snn
ENV HDFS_LOG_DIR /var/log/hadoop/$HDFS_USER
ENV HDFS_PID_DIR /var/run/hadoop/$HDFS_USER
ENV HADOOP_CONF_DIR /etc/hadoop/conf
ENV YARN_LOCAL_DIR /grid/hadoop/yarn/local /grid1/hadoop/yarn/local /grid2/hadoop/yarn/local
ENV YARN_LOG_DIR /var/log/hadoop/$YARN_USER
ENV YARN_LOCAL_LOG_DIR /grid/hadoop/yarn/logs /grid1/hadoop/yarn/logs /grid2/hadoop/yarn/logs
ENV YARN_PID_DIR /var/run/hadoop/$YARN_USER
ENV MAPRED_LOG_DIR /var/log/hadoop/$MAPRED_USER
ENV MAPRED_PID_DIR /var/run/hadoop/$MAPRED_USER
ENV HIVE_CONF_DIR /etc/hive/conf
ENV HIVE_LOG_DIR /var/log/hive
ENV HIVE_PID_DIR /var/run/hive
ENV WEBHCAT_CONF_DIR /etc/hcatalog/conf/webhcat
ENV WEBHCAT_LOG_DIR var/log/webhcat
ENV WEBHCAT_PID_DIR /var/run/webhcatDOOP_GROUP
ENV HBASE_CONF_DIR /etc/hbase/conf
ENV HBASE_LOG_DIR /var/log/hbase
ENV HBASE_PID_DIR /var/run/hbase
ENV ZOOKEEPER_DATA_DIR /grid1/hadoop/zookeeper/data
ENV ZOOKEEPER_CONF_DIR /etc/zookeeper/conf
ENV ZOOKEEPER_LOG_DIR /var/log/zookeeper
ENV ZOOKEEPER_PID_DIR /var/run/zookeeper
ENV PIG_CONF_DIR /etc/pig/conf
ENV PIG_LOG_DIR /var/log/pig
ENV PIG_PID_DIR /var/run/pig
ENV OOZIE_CONF_DIR /etc/oozie/conf
ENV OOZIE_DATA /var/db/oozie
ENV OOZIE_LOG_DIR /var/log/oozie
ENV OOZIE_PID_DIR /var/run/oozie
ENV OOZIE_TMP_DIR /var/tmp/oozie
ENV SQOOP_CONF_DIR /etc/sqoop/conf
ENV ACCUMULO_CONF_DIR /etc/accumulo/conf
ENV ACCUMULO_LOG_DIR /var/log/accumulo

RUN groupadd $HADOOP_GROUP; \
	groupadd $MAPRED_GROUP; \
	groupadd $NAGIOS_GROUP; \
	useradd -g $HADOOP_GROUP $HDFS_USER; \
	useradd -g $HADOOP_GROUP $YARN_USER; \
	useradd -g $HADOOP_GROUP -g $MAPRED_GROUP $MAPRED_USER; \
	useradd -g $HADOOP_GROUP $PIG_USER; \
	useradd -g $HADOOP_GROUP $HIVE_USER; \
	useradd -g $HADOOP_GROUP $WEBHCAT_USER; \
	useradd -g $HADOOP_GROUP $HBASE_USER; \
	useradd -g $HADOOP_GROUP $ZOOKEEPER_USER; \
	useradd -g $HADOOP_GROUP $OOZIE_USER; \
	useradd -g $HADOOP_GROUP $ACCUMULO_USER; \
	useradd -g $HADOOP_GROUP $FALCON_USER; \
	useradd -g $HADOOP_GROUP $SQOOP_USER; \
	useradd -g $HADOOP_GROUP $KAFKA_USER; \
	useradd -g $HADOOP_GROUP $STORM_USER; \
	useradd -g $HADOOP_GROUP $KNOX_USER; \
	useradd -g $NAGIOS_GROUP $NAGIOS_USER

# Install Hadoop packages
RUN yum -y update; \
	yum clean all; \
RUN yum -y install tar \
	vim \
	wget

# install Java 8
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.rpm" -O /tmp/jdk-8-linux-x64.rpm
RUN yum -y install /tmp/jdk-8-linux-x64.rpm

RUN alternatives --install /usr/bin/java jar /usr/java/latest/bin/java 200000; \
	alternatives --install /usr/bin/javaws javaws /usr/java/latest/bin/javaws 200000; \
	alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000

# Configuration of the remote repos
RUN wget -nv http://public-repo-1.hortonworks.com/HDP/centos6/2.x/GA/2.2.0.0/hdp.repo -O /etc/yum.repos.d/hdp.repo

# Disable SELinux & IPTables
#RUN sudo setenforce 0; \
#	chkconfig iptables off; \
#	service iptables stop 

# Download companion files
RUN mkdir -p /opt/hdp_install_helper
RUN wget -qO- http://public-repo-1.hortonworks.com/HDP/tools/2.2.0.0/hdp_manual_install_rpm_helper_files-2.2.0.0.2041.tar.gz \
	| tar zxvf - -C /opt/hdp_install_helper --strip 1

RUN yum -y install hadoop \
	hadoop-hdfs \
	hadoop-libhdfs \
	hadoop-yarn \
	hadoop-mapreduce \
	hadoop-client \
	openssl \
	snappy \
	snappy-devel \
	lzo \
	lzo-devel \
	hadooplzo \
	hadooplzo-native \
	zookeeper

# Create the directories for Hadoop
RUN mkdir -p $DFS_NAME_DIR \
	$FS_CHECKPOINT_DIR \
	$DFS_DATA_DIR \
	$YARN_LOCAL_DIR \
	$YARN_LOCAL_LOG_DIR \
	$HDFS_LOG_DIR \
	$YARN_LOG_DIR \
	$HDFS_PID_DIR \
	$YARN_PID_DIR \
	$MAPRED_LOG_DIR \
	$MAPRED_PID_DIR \
	$ZOOKEEPER_LOG_DIR \
	$ZOOKEEPER_PID_FILE \
	$ZOOKEEPER_DATA_DIR

RUN chown -R $HDFS_USER:$HADOOP_GROUP $DFS_NAME_DIR; chmod -R 755 $DFS_NAME_DIR; \
	chown -R $HDFS_USER:$HADOOP_GROUP $FS_CHECKPOINT_DIR; chmod -R 755 $FS_CHECKPOINT_DIR; \
	chown -R $HDFS_USER:$HADOOP_GROUP $DFS_DATA_DIR; chmod -R 750 $DFS_DATA_DIR; \
	chown -R $YARN_USER:$HADOOP_GROUP $YARN_LOCAL_DIR; chmod -R 755 $YARN_LOCAL_DIR; \
	chown -R $YARN_USER:$HADOOP_GROUP $YARN_LOCAL_LOG_DIR; chmod -R 755 $YARN_LOCAL_LOG_DIR; \
	chown -R $HDFS_USER:$HADOOP_GROUP $HDFS_LOG_DIR; chmod -R 755 $HDFS_LOG_DIR; \
	chown -R $YARN_USER:$HADOOP_GROUP $YARN_LOG_DIR; chmod -R 755 $YARN_LOG_DIR; \
	chown -R $HDFS_USER:$HADOOP_GROUP $HDFS_PID_DIR; chmod -R 755 $HDFS_PID_DIR; \
	chown -R $YARN_USER:$HADOOP_GROUP $YARN_PID_DIR; chmod -R 755 $YARN_PID_DIR; \
	chown -R $MAPRED_USER:$HADOOP_GROUP $MAPRED_LOG_DIR; chmod -R 755 $MAPRED_LOG_DIR; \
	chown -R $MAPRED_USER:$HADOOP_GROUP $MAPRED_PID_DIR; chmod -R 755 $MAPRED_PID_DIR; \
	chown -R $ZOOKEEPER_USER:$HADOOP_GROUP $ZOOKEEPER_LOG_DIR; chmod -R 755 $ZOOKEEPER_LOG_DIR; \
	chown -R $ZOOKEEPER_USER:$HADOOP_GROUP $ZOOKEEPER_PID_FILE; chmod -R 755 $ZOOKEEPER_PID_FILE; \
	chown -R $ZOOKEEPER_USER:$HADOOP_GROUP $ZOOKEEPER_DATA_DIR; chmod -R 755 $ZOOKEEPER_DATA_DIR

RUN 1 > $ZOOKEEPER_DATA_DIR/myid

#RUN hdp-select set all 2.2.0.0-2041


#hdfs fs -mkdir /user/<username>
#hdfs fs -chown <username>:<groupname> /user/<username>


