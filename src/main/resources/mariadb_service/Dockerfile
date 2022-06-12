FROM mariadb:latest

ENV MARIADB_ROOT_PASSWORD=1234
ENV MARIADB_DATABASE=echoserver
ENV MARIADB_USER=nayananga
ENV MARIADB_PASSWORD=password

ADD echoserver.sql /docker-entrypoint-initdb.d
COPY mariadb_service/config_file.conf /etc/mysql/conf.d


EXPOSE 3306
