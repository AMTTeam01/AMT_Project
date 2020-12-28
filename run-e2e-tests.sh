#!/bin/bash

# Start web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose -f docker-compose.yml -f docker-compose.local.yml build
docker-compose -f docker-compose.yml -f docker-compose.local.yml up -d

mvn liberty:stop

mvn clean package -Dmaven.test.skip=true
mvn liberty:create liberty:install-feature liberty:deploy
mvn liberty:start

cd e2e/
npm run test
