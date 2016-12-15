package io.pivotalservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;

/**
 * Create a 'Stub Runner' Server by using the @EnableStubRunnerServer annotation.
 *
 * This will use the configuration provided (e.g. application.properties) to look for the necessary 'stub' definitions
 * and then create a WireMock for each of them. The mocks operate on their own port as per the configuration given.
 */
@SpringBootApplication
@EnableStubRunnerServer
public class StubrunnerServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(StubrunnerServerApplication.class, args);
	}
}
