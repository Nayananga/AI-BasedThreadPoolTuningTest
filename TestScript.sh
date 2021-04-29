#!/bin/bash

##Concurrency 1

#Prime10k
java -jar -DLOG_FILE_NAME=Prime10k_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10k_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime100k
java -jar -DLOG_FILE_NAME=Prime100k_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime100k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime100k_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime10m
java -jar -DLOG_FILE_NAME=Prime10m_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10m_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
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
#Sqrt
java -jar -DLOG_FILE_NAME=Sqrt_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Sqrt 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Sqrt_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Factorial
java -jar -DLOG_FILE_NAME=Factorial_1 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Factorial 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/1_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Factorial_1.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000

##Concurrency 10

#Prime10k
java -jar -DLOG_FILE_NAME=Prime10k_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10k_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime100k
java -jar -DLOG_FILE_NAME=Prime100k_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime100k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime100k_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime10m
java -jar -DLOG_FILE_NAME=Prime10m_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10m_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
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
#Sqrt
java -jar -DLOG_FILE_NAME=Sqrt_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Sqrt 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Sqrt_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Factorial
java -jar -DLOG_FILE_NAME=Factorial_10 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Factorial 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/10_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Factorial_10.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000

##Concurrency 50

#Prime10k
java -jar -DLOG_FILE_NAME=Prime10k_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10k_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime100k
java -jar -DLOG_FILE_NAME=Prime100k_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime100k 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime100k_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime1m
java -jar -DLOG_FILE_NAME=Prime1m_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime1m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime1m_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Prime10m
java -jar -DLOG_FILE_NAME=Prime10m_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Prime10m 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Prime10m_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
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
#Sqrt
java -jar -DLOG_FILE_NAME=Sqrt_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Sqrt 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Sqrt_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
#Factorial
java -jar -DLOG_FILE_NAME=Factorial_50 target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar Factorial 10 99P &
java -jar ~/Downloads/apache-jmeter-5.4/bin/ApacheJMeter.jar -n -t ~/Desktop/jmeter_service/50_concurrency.jmx -l ~/Desktop/jmeter_service/jmeter_results_no_pretrained/Factorial_50.jtl -q ~/Desktop/jmeter_service/performance_common/distribution/scripts/jmeter/user.properties &
wait
fuser -k -n tcp 15000
exit