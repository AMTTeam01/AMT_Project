#!/bin/bash

until $(curl --output /dev/null --silent --head --fail http://localhost:9080); do
    printf '.'
    sleep 5
done


sleep 1m