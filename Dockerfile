FROM ubuntu:20.04

WORKDIR /home

RUN apt-get update -y \
    && apt install mysql-server -y

COPY src/main/resources/echoserver.sql .

RUN service mysql stop \
    && service mysql start \
    && mysql -e "CREATE USER 'nayananga'@'localhost' IDENTIFIED BY 'password'" \
    && mysql -e "GRANT ALL PRIVILEGES ON * . * TO 'nayananga'@'localhost'" \
    && mysql -e "CREATE DATABASE echoserver" \
    && mysql echoserver < echoserver.sql

ARG DEBIAN_FRONTEND=noninteractive

ENV TZ = Europe/Amsterdam

RUN apt-get install -y openjdk-8-jdk \
    && apt-get clean

RUN apt-get install ca-certificates-java \
    && apt-get clean \
    && update-ca-certificates -f

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

RUN export JAVA_HOME

COPY target/adaptive-concurrency-control-1.0-SNAPSHOT-jar-with-dependencies.jar .
