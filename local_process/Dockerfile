FROM ubuntu:15.04
MAINTAINER Antoine Hars <antoine.hars@gmail.com>

# maj system
RUN apt-get update
RUN apt-get install -y software-properties-common \
	wget

# install Java 8
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true \
	| /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

# install Apache Flume 1.5.2
RUN mkdir -p /opt/flume/logs && touch /opt/flume/logs/flume.log
RUN wget -qO- http://archive.apache.org/dist/flume/1.5.2/apache-flume-1.5.2-bin.tar.gz \
  | tar zxvf - -C /opt/flume --strip 1

# set variables
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle/
ENV PATH /opt/flume/bin:$PATH
ENV REPO /opt/hdp-flume-sandbox/
WORKDIR $REPO

# run Flume on some logs
CMD flume-ng agent -n agent_${CONF_FILE} -c conf -f conf/flume_${CONF_FILE}.conf -Dflume.root.logger=INFO,console

