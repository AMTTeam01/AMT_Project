#!/bin/bash

# Start web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose build
docker-compose up -d

sleep 300

curl -v http://localhost:9080/register

cd e2e/
npm install
npx codeceptjs run --steps
cd ..

# Restart web app and database
#docker-compose down
#rm -rf ./volumes/db
#docker-compose build
#docker-compose up -d db
