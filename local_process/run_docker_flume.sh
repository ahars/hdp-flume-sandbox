if [ $# -ne 1 ]; then 
	echo wrong number of parameters \(need one\)
	exit 0
fi

mv data/input/example.log.COMPLETED data/input/example.log
rm data/ACCEPTED/dest_1/*
rm data/ACCEPTED/dest_2/*
rm data/ACCEPTED/dest_3/*
rm data/ACCEPTED/dest_4/*
rm data/ACCEPTED/others/*
rm data/REJECTED/*

$(boot2docker shellinit)

docker build --rm -t ahars/hdp-flume-sandbox .
docker run -t -i --rm \
	--name flume \
	-v /Users/ahars/Github/hdp-flume-sandbox/local_process/:/opt/hdp-flume-sandbox/ \
	-e CONF_FILE=$1 \
	ahars/hdp-flume-sandbox

echo
echo "*********************************************************"
echo JOB FLUME $1
echo
echo INPUT
ls -lR data/input
echo
echo ACCEPTED
ls -lR data/ACCEPTED
echo
echo REJECTED
ls -lR data/REJECTED
echo
echo "*********************************************************"
echo

