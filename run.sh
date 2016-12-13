#!/usr/bin/env bash
rm -rf ~/.m2/repository/io/pivotalservices/cdc-microservice
echo "Purged the .m2 repository."
gradle -b cdc-microservice/build.gradle clean build publishToMavenLocal
gradle -b cdc-microservice-consumer/build.gradle clean build --info
