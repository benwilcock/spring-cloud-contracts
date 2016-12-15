# Contract Driven Development - Stub Runner Server

This is bonus content - an optional extra if you will.

> This project depends on the `cdc-microservice` project publishing it's contracts and mock server definitions to Maven locally. If they are not there, 
the server will have no definitions to host mocks for.

## Start the Sub Runner Server

```bash
gradle bootRun
```

> Note: The StubRunner server in this example uses two ports - a RANDOM one for the Spring Boot application and 6565 for the declared stubs. You can see whuch stubs are being hosted by calling the `/stubs` endpoint when the server has started.

Check the stubs are being hosted as planned...

```bash
$ curl localhost:<Your RANDOM here>/stubs 
{"io.pivotalservices:cdc-microservice:0.0.1-SNAPSHOT:stubs":6565}
```

You should now be able to interact with this Mock (stub) service by calling it in the usual way on port 6565. 

## How it works

Creating a Stub Runner Server is easy (once you have the right gradle dependencies). Simply annotate your `@SpringBootApplication` with `@EnableStubRunnerServer` as follows...

```java
@SpringBootApplication // Declare a Spring Boot application.
@EnableStubRunnerServer // Turn on the StubRunner function.
```

You'll then need to define which mock services should be offered by telling the Stub Runner where their definitions live in Maven and which port to use when hosting their mock.
 
> Note: You also tell the SpringBoot wrapper service which port this should be on. Here i've specified `0` or 'random'. Not sure if the wrapper server can be turned off?

```properties
# Tell the StubRunnerServer to use the Local Maven repo when looking for the stubs
stubrunner.workOffline=true 

# Tell the StubRunnerServer to look for the Maven stub jar under 'io.pivotalservices:cdc-microservice' and start on 6565
stubrunner.ids=io.pivotalservices:cdc-microservice:+:stubs:6565

# Tell the overarching SpringBoot app to use a random port, so it get's out of the way of other applications
server.port=0
```