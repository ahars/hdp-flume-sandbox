dir='/Users/ahars/Github/hdp-flume-sandbox/hadoop/'

$(boot2docker shellinit)

docker build --rm -t ahars/hdp-flume-sandbox-hadoop $dir
docker run -t -i --rm \
	--name flume-hadoop \
	ahars/hdp-flume-sandbox-hadoop /bin/bash

