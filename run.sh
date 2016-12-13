#!/usr/bin/env bash
rm -rf ~/.m2/repository/io/pivotalservices/cdc-microservice
gradle -b cdc-microservice/build.gradle clean build publishToMavenLocal
gradle -b cdc-microservice-consumer/build.gradle clean build --info
