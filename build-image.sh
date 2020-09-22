#!/bin/bash

#Run maven
mvn install 

# Exit if the build fail
if [[ "$?" -ne 0 ]] ; then
    echo 'could not perform tests' ; exit $rc
fi

# TODO change to our .war filename
cp target/mvc-simple.war docker/images/paygit ara/apps/