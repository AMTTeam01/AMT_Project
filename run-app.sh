#!/bin/bash

# Restart web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose build
docker-compose up