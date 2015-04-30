boot2docker start
$(boot2docker shellinit)

docker build --rm -t ahars/hdp-flume-sandbox .
docker run -t -i --rm --name flume ahars/hdp-flume-sandbox

