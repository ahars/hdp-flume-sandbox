if [ $# -ne 1 ]; then 
	echo wrong number of parameters \(need one\)
	exit 0
fi

dir='/Users/ahars/Github/hdp-flume-sandbox/local_process/'

mv $dir/data/input/example.log.COMPLETED $dir/data/input/example.log
rm $dir/data/ACCEPTED/dest_1/*
rm $dir/data/ACCEPTED/dest_2/*
rm $dir/data/ACCEPTED/dest_3/*
rm $dir/data/ACCEPTED/dest_4/*
rm $dir/data/ACCEPTED/others/*
rm $dir/data/REJECTED/*

$(boot2docker shellinit)

docker build --rm -t ahars/hdp-flume-sandbox $dir
docker run -t -i --rm \
	--name flume \
	-v $dir:/opt/hdp-flume-sandbox/ \
	-e CONF_FILE=$1 \
	ahars/hdp-flume-sandbox

echo
echo "*********************************************************"
echo RUN ON LOCAL - JOB FLUME $1
echo
echo INPUT
ls -lR $dir/data/input
echo
echo ACCEPTED
ls -lR $dir/data/ACCEPTED
echo
echo REJECTED
ls -lR $dir/data/REJECTED
echo
echo "*********************************************************"
echo

