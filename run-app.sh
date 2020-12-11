#!/bin/bash

# Rebuild project
mvn clean package -Dmaven.test.skip=true

# Restart web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose -f docker-compose.yml -f docker-compose.local.yml build
docker-compose -f docker-compose.yml -f docker-compose.local.yml up -d
