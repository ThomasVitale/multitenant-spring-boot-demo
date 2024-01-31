package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestInstrumentServiceApplication {

	@Bean
	@Scope("singleton") // needed because of https://github.com/spring-projects/spring-boot/issues/35786
	GenericContainer<?> demo(DynamicPropertyRegistry properties) {
		var ollama = new GenericContainer<>("redis:7.2");
		properties.add("myapp.tenants[0].identifier",
				() -> "john");
		properties.add("myapp.tenants[0].schema",
				() -> "john1");
		return ollama;
	}

	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>("postgres:15.5");
	}

	public static void main(String[] args) {
		SpringApplication.from(InstrumentServiceApplication::main)
				.with(TestInstrumentServiceApplication.class)
				.run(args);
	}

}
