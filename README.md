# hdp-flume-sandbox

## Execute
Two ways to process logs with Apache Flume 1.5.2
* Docker with local file system
```
./run_flume_local.sh "JOB_NUMBER"
```

* Docker with hadoop file system
```
./run_flume_hadoop.sh
./launch_hadoop.sh
./run_flume.sh "JOB_NUMBER"
```

