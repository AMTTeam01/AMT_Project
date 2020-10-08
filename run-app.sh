#!/bin/bash

# Restart web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose build
docker-compose up

# Create database with the file init
cat database.sql | docker exec -i help2000_db_container mysql -u dbDevHelp2000 -p devpass help2000
