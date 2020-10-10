#!/bin/bash

# Rebuild project
mvn clean package

# Restart web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose build
docker-compose up -d db