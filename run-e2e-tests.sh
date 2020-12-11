#!/bin/bash

# Start web app and database
sh run-app.sh

cd e2e/
ls
npm install
npm run test

# Restart web app and database
#docker-compose down
#rm -rf ./volumes/db
#docker-compose build
#docker-compose up -d db
