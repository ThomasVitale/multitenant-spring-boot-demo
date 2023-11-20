package com.thomasvitale.instrumentservice.multitenancy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "multitenancy.http")
public record TenantHttpProperties(
		@DefaultValue("X-TenantId")
		String headerName
){}
