package com.thomasvitale.instrumentservice;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "myapp")
public class MyProperties {
	private final List<Tenant> tenants = new ArrayList<>();

	public List<Tenant> getTenants() {
		return tenants;
	}
}
