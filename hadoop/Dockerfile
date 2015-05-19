FROM centos:6
MAINTAINER Antoine Hars <antoine.hars@gmail.com>

# Environment variables for Hadoop
ENV JAVA_HOME /usr/java/latest
ENV DFS_NAME_DIR /grid/hadoop/hdfs/nn /grid1/hadoop/hdfs/nn
ENV DFS_DATA_DIR /grid/hadoop/hdfs/dn /grid1/hadoop/hdfs/dn /grid2/hadoop/hdfs/dn
ENV FS_CHECKPOINT_DIR /grid/hadoop/hdfs/snn /grid1/hadoop/hdfs/snn /grid2/hadoop/hdfs/snn
ENV HDFS_LOG_DIR /var/log/hadoop/hdfs
ENV HDFS_PID_DIR /var/run/hadoop/hdfs
ENV HADOOP_CONF_DIR /etc/hadoop/conf
ENV YARN_LOCAL_DIR /grid/hadoop/yarn/local /grid1/hadoop/yarn/local /grid2/hadoop/yarn/local
ENV YARN_LOG_DIR /var/log/hadoop/yarn
ENV YARN_LOCAL_LOG_DIR /grid/hadoop/yarn/logs /grid1/hadoop/yarn/logs /grid2/hadoop/yarn/logs
ENV YARN_PID_DIR /var/run/hadoop/yarn
ENV MAPRED_LOG_DIR /var/log/hadoop/mapred
ENV MAPRED_PID_DIR /var/run/hadoop/mapred
ENV HIVE_CONF_DIR /etc/hive/conf
ENV HIVE_LOG_DIR /var/log/hive
ENV HIVE_PID_DIR /var/run/hive

# environment preparation
RUN yum -y update; \
	yum clean all 
RUN yum -y install tar \
	vim \
	wget \
	maven

# install Java 8
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.rpm" -O /tmp/jdk-8-linux-x64.rpm; \
	yum -y install /tmp/jdk-8-linux-x64.rpm; \
	alternatives --install /usr/bin/java jar /usr/java/latest/bin/java 200000; \
	alternatives --install /usr/bin/javaws javaws /usr/java/latest/bin/javaws 200000; \
	alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000

# Configuration of the remote repos
RUN wget -nv http://public-repo-1.hortonworks.com/HDP/centos6/2.x/GA/2.2.0.0/hdp.repo -O /etc/yum.repos.d/hdp.repo

# Install the Hadoop packages
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
	hive-hcatalog \
	mysql-connector-java* \
	flume \
	flume-agent

# Download companion files
RUN mkdir -p /opt/hdp_install_helper
RUN wget -qO- http://public-repo-1.hortonworks.com/HDP/tools/2.2.0.0/hdp_manual_install_rpm_helper_files-2.2.0.0.2041.tar.gz \
	| tar zxvf - -C /opt/hdp_install_helper --strip 1

# Disable SELinux & IPTables
RUN setenforce 0 ; \
	chkconfig iptables off

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
		$MAPRED_PID_DIR; \
	chmod -R 755 $DFS_NAME_DIR \
		$FS_CHECKPOINT_DIR \
		$YARN_LOCAL_DIR \
		$YARN_LOCAL_LOG_DIR \
		$HDFS_LOG_DIR \
		$YARN_LOG_DIR \
		$HDFS_PID_DIR \
		$YARN_PID_DIR \
		$MAPRED_LOG_DIR \
		$MAPRED_PID_DIR; \
	chmod -R 750 $DFS_DATA_DIR

RUN hdp-select set all 2.2.0.0-2041

# Configuration of HDFS
RUN sed -i -e "s/TODO-NAMENODE-HOSTNAME:PORT/localhost:8020/g" /opt/hdp_install_helper/configuration_files/core_hadoop/core-site.xml; \
	sed -i -e "s/TODO-DFS-NAME-DIR/$(echo $DFS_NAME_DIR | sed -e "s/ /,/g" | sed -e "s/\//\\\\\//g")/g" /opt/hdp_install_helper/configuration_files/core_hadoop/hdfs-site.xml; \
	sed -i -e "s/TODO-DFS-DATA-DIR/$(echo $DFS_DATA_DIR | sed -e "s/ /,/g" | sed -e "s/\//\\\\\//g")/g" /opt/hdp_install_helper/configuration_files/core_hadoop/hdfs-site.xml; \
	sed -i -e "s/TODO-NAMENODE-HOSTNAME/localhost/g" /opt/hdp_install_helper/configuration_files/core_hadoop/hdfs-site.xml; \
	sed -i -e "s/TODO-SECONDARYNAMENODE-HOSTNAME/localhost/g" /opt/hdp_install_helper/configuration_files/core_hadoop/hdfs-site.xml; \
	sed -i -e "s/TODO-FS-CHECKPOINT-DIR/$(echo $FS_CHECKPOINT_DIR | sed -e "s/ /,/g" | sed -e "s/\//\\\\\//g")/g" /opt/hdp_install_helper/configuration_files/core_hadoop/hdfs-site.xml; \
	sed -i -e "s/TODO-RESOURCEMANAGERNODE-HOSTNAME/localhost/g" /opt/hdp_install_helper/configuration_files/core_hadoop/yarn-site.xml; \
	sed -i -e "s/TODO-YARN-LOCAL-DIR/$(echo $YARN_LOCAL_DIR | sed -e "s/ /,/g" | sed -e "s/\//\\\\\//g")/g" /opt/hdp_install_helper/configuration_files/core_hadoop/yarn-site.xml; \
	sed -i -e "s/TODO-YARN-LOCAL-LOG-DIR/$(echo $YARN_LOCAL_LOG_DIR | sed -e "s/ /,/g" | sed -e "s/\//\\\\\//g")/g" /opt/hdp_install_helper/configuration_files/core_hadoop/yarn-site.xml; \
	sed -i -e "s/TODO-JOBHISTORYNODE-HOSTNAME/localhost/g" /opt/hdp_install_helper/configuration_files/core_hadoop/yarn-site.xml; \
	sed -i -e "s/TODO-JOBHISTORYNODE-HOSTNAME/localhost/g" /opt/hdp_install_helper/configuration_files/core_hadoop/mapred-site.xml

RUN rm -r $HADOOP_CONF_DIR; \
	mkdir -p $HADOOP_CONF_DIR; \
	cp -R /opt/hdp_install_helper/configuration_files/* $HADOOP_CONF_DIR; \
	cp $HADOOP_CONF_DIR/core_hadoop/core-site.xml $HADOOP_CONF_DIR/; \
	chmod -R 755 $HADOOP_CONF_DIR/../ 

# Configuration of Flume
RUN cp /etc/flume/conf/flume-env.sh.template /etc/flume/conf/flume-env.sh; \
	echo >> /etc/flume/conf/flume-env.sh; \
	echo 'JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=4159 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Xms100m -Xmx4000m"' >> /etc/flume/conf/flume-env.sh

ENV REPO /opt/hdp-flume-sandbox/
WORKDIR $REPO

