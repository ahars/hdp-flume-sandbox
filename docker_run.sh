if [ $# -ne 1 ]; then 
	echo wrong number of parameters \(need one\)
	exit 0
fi

mv data/logs/input_1.log.COMPLETED data/logs/input_1.log
rm data/ACCEPTED/dest_1/*
rm REJECTED/*

boot2docker start
$(boot2docker shellinit)

docker build --rm -t ahars/hdp-flume-sandbox .
docker run -t -i --rm \
	--name flume \
	-v /Users/ahars/Github/hdp-flume-sandbox/:/opt/hdp-flume-sandbox \
	-e CONF_FILE=$1 \
	ahars/hdp-flume-sandbox

ls -lR data/ACCEPTED
ls -lR data/REJECTED
