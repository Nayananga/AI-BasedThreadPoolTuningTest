FROM mysql/mysql-server:latest


ENV MYSQL_ALLOW_EMPTY_PASSWORD=true
ENV MYSQL_USER=nayananga
ENV MYSQL_PASSWORD=password
ENV MYSQL_DATABASE=echoserver

COPY echoserver.sql /docker-entrypoint-initdb.d/