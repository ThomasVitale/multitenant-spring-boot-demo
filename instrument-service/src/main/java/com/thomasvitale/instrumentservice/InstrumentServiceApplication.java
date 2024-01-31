package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
public class InstrumentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstrumentServiceApplication.class, args);
	}

}

@RestController
class DemoController {

	private final MyProperties myProperties;

	DemoController(MyProperties myProperties) {
		this.myProperties = myProperties;
	}

	@GetMapping("/")
	String getIdentifier() {
		return myProperties.getTenants().stream().map(Tenant::getIdentifier).collect(Collectors.joining(","));
	}

	@GetMapping("/schema")
	String getSchema() {
		return myProperties.getTenants().stream().map(Tenant::getSchema).collect(Collectors.joining(","));
	}
}


