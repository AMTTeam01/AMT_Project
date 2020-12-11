#!/bin/bash

# Start web app and database
sh run-app.sh

until $(curl --output /dev/null --silent --head --fail http://localhost:9080); do
    printf '.'
    sleep 5
done

docker-compose -f docker-compose.yml -f docker-compose.local.yml build codecpetjs
docker-compose -f docker-compose.yml -f docker-compose.local.yml up codecpetjs

docker-compose down --remove-orphan



# Restart web app and database
#docker-compose down
#rm -rf ./volumes/db
#docker-compose build
#docker-compose up -d db
