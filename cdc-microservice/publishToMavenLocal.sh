#!/usr/bin/env bash
rm -rf ~/.m2/repository/io/pivotalservices/
gradle clean build generateWireMockClientStubs publishToMavenLocal $1
