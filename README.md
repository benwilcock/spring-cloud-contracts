# Consumer Driven Contracts

This project demonstrates how you can use [Spring-Cloud-Contract][a] to acheive *Consumer Driven Contracts* in your [Spring Boot][b] microservice development.

## Introduction

This project is separated into two sub-projects as follows...

  **Project A.** The `cdc-micsroservice` - a simple Spring Boot microservice that has been setup to both offer use a consumer-driven-contract (or CDC for short). The contract describes the expected inputs and outputs from the service that consumers of the service should base their code upon.

  **Project B.** The `cdc-microservice-consumer` - a simple spring boot app that in production has a runtime dependency on the service offered by the `cdc-microservice`. As a dependent, this consumer is particularly keen to have a contract in place that describes the interactions it can expect with the service.

Project A. contains the contract definitions that describe the expected behaviour of the service. After a successful build these definitions are published so that they are available for consumers to use when creating integrations and interactions.

Project B. can obtain these contract definitions and use them when testing to create a 'mock' service that behaves as described by these behavioural descriptions. This reduces the number of test-time dependencies between these two projects and allows the consumer to test their code in a more isolated manner.

For more information on project (A) take a look at the README in the `cdc-microservice` project folder. For more information on project (B) take a look at the README in the `cdc-microservice-consumer` project folder.

## Running the demo

Starting in this folder, we first run the build and publish steps for the service (Project A). This will check the microservice can be compiled and run and will export the contract definitions and behaviours to the local Maven repository.

```bash
$ gradle -b cdc-microservice/build.gradle clean build publishToMavenLocal
```

You should see `BUILD SUCCESSFUL` and the following output towards the end of the gradle command:-

```bash
:build
:generatePomFileForStubsPublication
:publishStubsPublicationToMavenLocal
:publishToMavenLocal
```

Next, we build and run the consumer (Project B). This will extract the contract definitions and behaviours from the Maven repository and use them to create a Mock service to test against.

```bash
$ gradle -b cdc-microservice-consumer/build.gradle clean build --info
```

Again, you should ultimately see `BUILD SUCCESSFUL`. The sharp sighted can also search the output for lines similar to the following that show the 'Stub Runner' in action:-

```bash
2016-12-13 10:12:25.603  INFO 3114 --- [    Test worker] o.s.c.contract.stubrunner.StubServer     : Started stub server for project [io.pivotalservices:cdc-microservice:0.0.1-SNAPSHOT:stubs] on port 6565
2016-12-13 10:12:25.859  INFO 3114 --- [    Test worker] o.s.c.c.stubrunner.StubRunnerExecutor    : All stubs are now running RunningStubs [namesAndPorts={io.pivotalservices:cdc-microservice:0.0.1-SNAPSHOT:stubs=6565}]
```

There is a script that will perform the above steps for you. See `run.sh`.

## Words of caution

1. **Spring Cloud Contract is version 1.0 software.** It can be a bit tempramental and will be subject to a great deal of change over the coming months. You should be aware from the outset that it's not particularly forgiving when you start refactoring your code. It can also fail silently so you have no idea that something you changed has had an impact until much later. See point (2) below.

2. **Keep an eye on your `build` folders.** This CDC technique uses a blast from the past - automatic code generation for generating the JUnit tests for the service based on your specified service contracts. This mostly works, but if you want to run the tests it generates in your IDE be prepared for a bit of messing about in the settings and setup. Sometimes, when your setup changes, this auto-generation can silently fail.

3. **Don't use the Gradle wrapper.** I found that the resilts can be less predictable when using the wrapper style, hence why I have purposely not included the wrapper in the source as I normally do.

4. **Maven repositories are an integral part of the solution.** In particular for this demo, the maven local repository often known as `.m2` is the glue between projects. It's the location where the service contracts and the wiremock stub instructions are shared between projects. In a gradle environment, this may cause extra headaches. In gradle, you also have to specifically 'publish' these artifacts to the repository each time.

[a]: https://cloud.spring.io/spring-cloud-contract/
[b]: https://projects.spring.io/spring-boot/
