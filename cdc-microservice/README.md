## Consumer Driven Contract - Service Side

This project illustrates using Spring Cloud Contract's verifier to verify a service's contract is being upheld. This is done using input and output scripts (groovy) that define the behaviour. During the process, JUnit tests are automatically generated based on these 'service contracts' and run against the service's controller code during the build phase. If the build is successful, these definitions are then packaged up and placed into the local Maven repository for use by service consumers.

### Setting up the Project

The first step is to setup the build script and bring in the Spring Cloud Contract dependencies. I'm not going to pring the whole `build.gradle` file here but the highlights include...

1. The plugins being applied...

  ```groovy
  apply plugin: 'spring-cloud-contract'
  apply plugin: 'maven-publish'
  ```

  This add's to Gradle the capability to publish the service consumer-driven-contract definitions to the local Maven repository.

2. The test's base-class package...

  ```groovy
  contracts {
      packageWithBaseClasses = 'io.pivotalservices'
  }
  ```

  It's in this package in the folder `src/test/java/io/pivotalservices` that I have included the mandatory base class for the auto generated tests.

3. Add the testing base-class for the suto-generated tests to extend.

  ```java
  package io.pivotalservices;

  import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
  import io.pivotalservices.fraud.FraudDetectionController;
  import org.junit.Before;

  public class ContractVerifierBase {

      @Before
      public void setup() {
          RestAssuredMockMvc.standaloneSetup(new FraudDetectionController()); // Set up the programmable Mock for the Controller.
      }
  }
  ```

  This class is used to setup the programmeable Mock for testing the controller.

### Setup the Contracts

Once the project is setup, it's time to add some contracts. The service contracts are located in the folder `src/test/resources/contracts`. They are Groovy documents that look similar to this:-

```groovy
package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url '/fraudcheck'
        body("""
    {
      "clientId":"1234567890",
      "loanAmount":99999
    }
    """)
        headers {
            header('Content-Type', 'application/vnd.fraud.v1+json')
        }
    }
    response {
        status 200
        body("""
  {
    "fraudCheckStatus": "FRAUD",
    "resultText": "Amount too high"
  }
  """)
        headers {
            header('Content-Type': 'application/vnd.fraud.v1+json;charset=UTF-8')
        }
    }
}
```

They describe the inputs and the outputs (requests and responses) expected when interacting with the `FraudDetectionController` (which is a Spring Boot microservice whose source code can be found in the `src/main/java/io/pivotalservices/fraud` folder).

### Build and Test the Service logic

To generate and run the Consumer Driven Contract test verifier, execute the following:-

````bash
$ gradle clean build
````

When you do this, a suite of Java tests are auto generated as part of th build. You can find these auto generated tests in `build/generated-test-sources` (in Gradle). You should go and examine these tests. If the process didn't work for some reason, the generated-test-sources folder may be empty!

When successfully generated, the tests produced look something like this:-

```java
@Test
public void validate_shouldMarkClientAsFraud() throws Exception {
    // given:
    MockMvcRequestSpecification request = given()
            .header("Content-Type", "application/vnd.fraud.v1+json")
            .body("{\"clientId\":\"1234567890\",\"loanAmount\":99999}");

    // when:
    ResponseOptions response = given().spec(request)
            .put("/fraudcheck");

    // then:
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.header("Content-Type")).isEqualTo("application/vnd.fraud.v1+json;charset=UTF-8");
    // and:
    DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
    assertThatJson(parsedJson).field("resultText").isEqualTo("Amount too high");
    assertThatJson(parsedJson).field("fraudCheckStatus").isEqualTo("FRAUD");
}
```

These tests are executed as part of the `gradle build`. If they fail, the build will fail in the usual way.

### Exporting the contracts

If the build and test process was successful, it's time to export the contracts and the service's behavioral definitions to Maven.

Before you start, check the `build` folder again. Within it you should have a `stubs` folder containing `contracts` and `mappings`. These are the artifacts that will be published to Maven in the `cdc-microservice-0.0.1-SNAPSHOT-stubs.jar`. Consumers can depend on this archive for the definitions of the service's contract and behaviours. They can use it when testing by having these definitions shape a 'Mock' service.

To export the contracts, use the `publishToMavenLocal` task in gradle:-

```bash
$ gradle publishToMavenLocal
```

As a final check, go to the folder `~/.m2/repository/io/pivotalservices` you should see a `/cdc-microservice/0.0.1-SNAPSHOT` folder containing a POM and our stubs.jar file. The date should be just seconds ago.

```bash
$ ls -la
-rw-r--r--  1 benwilcock  staff  2372 13 Dec 11:28 cdc-microservice-0.0.1-SNAPSHOT-stubs.jar
-rw-r--r--  1 benwilcock  staff  1034 13 Dec 11:35 cdc-microservice-0.0.1-SNAPSHOT.pom
```

That's it. Our contracts have been used to verify our service and have been exported so that consumers can use them when testing their service interactions.
