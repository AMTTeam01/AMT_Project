#!/bin/bash

# Run database
#docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 \
#           --name mysql-liberty \
#           -e MYSQL_DATABASE=help2000_db \
#           -e MYSQL_USER=dbDevHelp2000 \
#           -e MYSQL_PASSWORD=devpass \
#           -p 9906:9906 \
#           mcr.microsoft.com/mssql/server:2019-GA-ubuntu-16.04

docker-compose up