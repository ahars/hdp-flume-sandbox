FROM ubuntu:15.04
MAINTAINER Antoine Hars <antoine.hars@gmail.com>

RUN apt-get update
RUN apt-get install -y software-properties-common \
	wget

RUN wget -qO- http://www.apache.org/dyn/closer.cgi/flume/1.5.2/apache-flume-1.5.2-bin.tar.gz
