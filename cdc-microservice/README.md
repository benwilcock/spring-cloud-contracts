## Consumer Driven Contract - Service Side

This project illustrates using Spring Cloud Contract's verifier to verify a service using Groovy based input and output definitions. During the process, JUnit tests based on these contracts are automatically generated and executed against the business logic in the controller.

### Setup the Contracts

The contracts are located in the folder `src/test/resources/contracts/fraud`. They are Groovy documents that look similar to this:-

```groovy
package contracts.fraud

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
    "rejectionReason": "Amount too high"
  }
  """)
        headers {
            header('Content-Type': 'application/vnd.fraud.v1+json;charset=UTF-8')
        }
    }
}
```

They describe the inputs and the outputs expected by the `FraudDetectionController` which is a Spring Boot microservice whose source you can see in the `src/main/java/io/pivotalservices` folder.

### Setup the 'Base' class for testing
 
Because the contracts are stored under contracts.**fraud** a base class called **Fraud**Base must be added in the test folder. You can see it under `test/java/io.pivotalservices` in the source code directory. The base class sets up the RestAssured MockMvc framework to mock out the `FraudDetectionController` class and it looks similar to this...

```java
package io.pivotalservices;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public class FraudBase {

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new FraudDetectionController()); // Set up the programmable Mock for the Controller.
    }
}
```

### Build and Test

To generate and run the Consumer Driven Contract test verifier, execute the following:-

````bash
./gradlew clean build publishToMavenLocal
````

or 

````bash
./publishToMavenLocal.sh
````

When you do this, a suite of Java tests are auto generated as part of th build. You can find these auto generated tests in `build/generated-test-sources` (in Gradle).

The tests that are generated look something like this:-

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
    assertThatJson(parsedJson).field("rejectionReason").isEqualTo("Amount too high");
    assertThatJson(parsedJson).field("fraudCheckStatus").isEqualTo("FRAUD");
}
```

These tests are executed as part of the build. If they fail, the build will fail in the usual way.