#!/bin/bash

mvn liberty:stop

mvn clean package -Dmaven.test.skip=true
mvn liberty:create liberty:install-feature liberty:deploy
mvn liberty:start
#mvn liberty:configure-arquillian

# mvn verify
mvn failsafe:integration-test
#mvn liberty:stop
