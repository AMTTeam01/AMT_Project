#!/bin/bash

#Run maven
mvn install 

# Exit if the build fail
if [[ "$?" -ne 0 ]] ; then
    echo 'could not perform tests' ; exit $rc
fi

if [ ! -d "docker/images/payara/apps/" ] 
then
    echo 'create docker/images/payara/apps/ directory'
    mkdir docker/images/payara/apps/
fi

# TODO change to our .war filename
cp target/mvc-simple.war docker/images/payara/apps/