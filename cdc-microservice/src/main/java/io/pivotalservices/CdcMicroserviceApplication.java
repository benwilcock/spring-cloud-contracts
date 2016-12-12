package io.pivotalservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient
@SpringBootApplication
public class CdcMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdcMicroserviceApplication.class, args);
	}
}
