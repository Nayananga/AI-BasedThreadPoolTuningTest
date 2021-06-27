FROM mysql:latest

ENV MYSQL_ROOT_PASSWORD=1234
ENV MYSQL_DATABASE=echoserver
ENV MYSQL_USER=nayananga
ENV MYSQL_PASSWORD=password

ADD echoserver.sql /docker-entrypoint-initdb.d

EXPOSE 3306
