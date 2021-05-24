#!/bin/bash

docker kill some-redis
docker rm some-redis
docker run --name some-redis -d -p 6379:6379 redis
fuser -k -n tcp 15000

##Concurrency 1

#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbWrite
java -jar -DLOG_FILE_NAME=DbWrite_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbWrite 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbWrite_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbRead
java -jar -DLOG_FILE_NAME=DbRead_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbRead 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbRead_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000

##Concurrency 10

#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbWrite
java -jar -DLOG_FILE_NAME=DbWrite_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbWrite 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbWrite_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbRead
java -jar -DLOG_FILE_NAME=DbRead_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbRead 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbRead_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000

##Concurrency 50

#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbWrite
java -jar -DLOG_FILE_NAME=DbWrite_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbWrite 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbWrite_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#DbRead
java -jar -DLOG_FILE_NAME=DbRead_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar DbRead 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/DbRead_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
exit
